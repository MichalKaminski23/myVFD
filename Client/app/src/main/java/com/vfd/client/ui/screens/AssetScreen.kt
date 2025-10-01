package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.buttons.AppLoadMoreButton
import com.vfd.client.ui.components.cards.AppAssetCard
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.elements.AppSearchBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun AssetScreen(
    assetViewModel: AssetViewModel,
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState

) {
    val assetUiState by assetViewModel.assetUiState.collectAsState()
    val assetTypeUiState by assetTypeViewModel.assetTypeUiState.collectAsState()
    val assetUpdateUiState by assetViewModel.assetUpdateUiState.collectAsState()
    var editingAssetId by remember { mutableStateOf<Int?>(null) }

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = assetUiState.page + 1 < assetUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    val filteredAssets = assetUiState.assets.filter {
        val fullInfo = "${it.name} ${it.assetTypeName} ${it.description.orEmpty()}"
        searchQuery.isBlank() ||
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.assetTypeName.contains(searchQuery, ignoreCase = true) ||
                it.description?.contains(searchQuery, ignoreCase = true) == true ||
                fullInfo.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        assetViewModel.getAssets(page = 0, refresh = true)
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.AssetScreen -> assetViewModel.getAssets(page = 0, refresh = true)
                else -> {}
            }
        }
    }

    AppUiEvents(assetViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(assetUpdateUiState.success) {
        if (assetUpdateUiState.success) {
            editingAssetId = null
            assetViewModel.onAssetUpdateValueChange { it.copy(success = false) }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search assets...",
                enabled = !assetUiState.isLoading,
                loading = assetUiState.isLoading,
            )
            Spacer(Modifier.height(12.dp))
        }

        if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.USER.toString()) {
            item {
                AppText(
                    "You do not have permission to view assets.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            if (filteredAssets.isEmpty()) {
                item {
                    AppText(
                        if (searchQuery.isBlank())
                            "There aren't any assets in your VFD or the assets are still loading"
                        else
                            "No assets match your search",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            } else {
                items(filteredAssets) { asset ->
                    if (editingAssetId == asset.assetId) {
                        if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                            AppAssetCard(
                                asset,
                                actions = {
                                    AppTextField(
                                        value = assetUpdateUiState.name,
                                        onValueChange = { new ->
                                            assetViewModel.onAssetUpdateValueChange {
                                                it.copy(name = new)
                                            }
                                        },
                                        label = "Name",
                                        errorMessage = null
                                    )

                                    AppDropdown(
                                        items = assetTypeUiState.assetTypes,
                                        selectedCode = assetUpdateUiState.assetType,
                                        codeSelector = { it.assetType },
                                        labelSelector = { it.name },
                                        label = "Choose asset type",
                                        onSelected = { assetType ->
                                            assetViewModel.onAssetUpdateValueChange {
                                                it.copy(assetType = assetType.assetType)
                                            }
                                        },
                                        onLoadMore = {
                                            if (assetTypeUiState.page + 1 < assetTypeUiState.totalPages) {
                                                assetTypeViewModel.getAllAssetTypes(
                                                    page = assetTypeUiState.page + 1
                                                )
                                            }
                                        },
                                        hasMore = assetTypeUiState.page + 1 < assetTypeUiState.totalPages,
                                        onExpand = {
                                            if (assetTypeUiState.assetTypes.isEmpty())
                                                assetTypeViewModel.getAllAssetTypes(page = 0)
                                        },
                                        icon = Icons.Default.Build
                                    )

                                    AppTextField(
                                        value = assetUpdateUiState.description,
                                        onValueChange = { new ->
                                            assetViewModel.onAssetUpdateValueChange {
                                                it.copy(description = new)
                                            }
                                        },
                                        label = "Description",
                                        errorMessage = null,
                                        singleLine = false
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        AppButton(
                                            icon = Icons.Default.Check,
                                            label = "Save",
                                            onClick = {
                                                asset.assetId.let { id ->
                                                    val assetDto = AssetDtos.AssetPatch(
                                                        name = assetUpdateUiState.name,
                                                        assetType = assetUpdateUiState.assetType.takeIf { it.isNotBlank() },
                                                        description = assetUpdateUiState.description
                                                    )
                                                    assetViewModel.updateAsset(id, assetDto)
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            enabled = assetUpdateUiState.name.isNotBlank() &&
                                                    assetUpdateUiState.description.isNotBlank() &&
                                                    !assetUpdateUiState.isLoading,
                                            loading = assetUpdateUiState.isLoading
                                        )
                                        AppButton(
                                            icon = Icons.Default.Close,
                                            label = "Cancel",
                                            onClick = { editingAssetId = null },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            )
                            Spacer(Modifier.height(12.dp))
                        } else {
                            AppAssetCard(asset)
                        }
                    } else {
                        AppAssetCard(
                            asset,
                            actions = {
                                if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                                    AppButton(
                                        icon = Icons.Default.Edit,
                                        label = "Edit",
                                        onClick = {
                                            editingAssetId = asset.assetId
                                            assetViewModel.onAssetUpdateValueChange {
                                                it.copy(
                                                    name = asset.name,
                                                    assetType = "",
                                                    description = asset.description ?: ""
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            AppLoadMoreButton(
                hasMore = hasMore,
                isLoading = assetUiState.isLoading,
                onLoadMore = {
                    if (hasMore && !assetUiState.isLoading)
                        assetViewModel.getAssets(page = assetUiState.page + 1)
                }
            )
        }

        if (assetUpdateUiState.errorMessage != null) {
            item {
                AppErrorText(assetUpdateUiState.errorMessage!!)
            }
        }
    }
}
