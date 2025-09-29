package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.vfd.client.ui.components.AppAssetCard
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppDropdown
import com.vfd.client.ui.components.AppErrorText
import com.vfd.client.ui.components.AppText
import com.vfd.client.ui.components.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel
import com.vfd.client.ui.viewmodels.UiEvent

@Composable
fun AssetScreen(
    assetViewModel: AssetViewModel = hiltViewModel(),
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {

    val assetUiState by assetViewModel.assetUiState.collectAsState()

    val assetTypeUiState by assetTypeViewModel.assetTypeUiState.collectAsState()

    val assetUpdateUiState by assetViewModel.assetUpdateUiState.collectAsState()
    var editingAssetId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        assetViewModel.getAssets()
    }

    LaunchedEffect(Unit) {
        assetViewModel.events.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "OK"
                    )
                }

                is UiEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true
                    )
                }
            }
        }
    } // Snackbar

    LaunchedEffect(assetUpdateUiState.success) {
        if (assetUpdateUiState.success) {
            editingAssetId = null
            assetViewModel.onAssetUpdateValueChange { it.copy(success = false) }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        if (assetUiState.assets.isEmpty()) {
            AppText(
                "There aren't any assets in your VFD",
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            assetUiState.assets.forEach { asset ->
                if (editingAssetId == asset.assetId) {
                    AppAssetCard(
                        asset,
                        actions = {
                            AppTextField(
                                value = assetUpdateUiState.name,
                                onValueChange = { new ->
                                    assetViewModel.onAssetUpdateValueChange {
                                        it.copy(
                                            name = new
                                        )
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
                                        it.copy(
                                            assetType = assetType.assetType
                                        )
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
                                        it.copy(
                                            description = new
                                        )
                                    }
                                },
                                label = "Description",
                                errorMessage = null,
                                singleLine = false
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
                            {
                                AppButton(
                                    icon = Icons.Default.Check,
                                    label = "Save",
                                    onClick = {
                                        asset.assetId.let {
                                            val assetDto = AssetDtos.AssetPatch(
                                                name = assetUpdateUiState.name,
                                                assetType = assetUpdateUiState.assetType.takeIf { it.isNotBlank() },
                                                description = assetUpdateUiState.description
                                            )
                                            assetViewModel.updateAsset(
                                                it,
                                                assetDto
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = assetUpdateUiState.name.isNotBlank() && assetUpdateUiState.description.isNotBlank()
                                            && !assetUpdateUiState.isLoading,
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
                } else {
                    AppAssetCard(
                        asset, {
                            AppButton(
                                icon = Icons.Default.Edit,
                                label = "Edit",
                                onClick = {
                                    editingAssetId = asset.assetId
                                    assetViewModel.onAssetUpdateValueChange {
                                        it.copy(
                                            name = asset.name,
                                            assetType = "",
                                            description = asset.description!!
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }

        if (assetUpdateUiState.errorMessage != null) {
            AppErrorText(assetUpdateUiState.errorMessage!!)
        }
    }
}