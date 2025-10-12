package com.vfd.client.ui.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
import com.vfd.client.ui.components.cards.AppAssetCard
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.InspectionViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager
import com.vfd.client.utils.daysUntilSomething
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AssetScreen(
    assetViewModel: AssetViewModel,
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    inspectionViewModel: InspectionViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val assetUiState by assetViewModel.assetUiState.collectAsState()
    val assetTypeUiState by assetTypeViewModel.assetTypeUiState.collectAsState()
    val assetUpdateUiState by assetViewModel.assetUpdateUiState.collectAsState()
    var editingAssetId by remember { mutableStateOf<Int?>(null) }

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = assetUiState.page + 1 < assetUiState.totalPages

    val inspectionUiState by inspectionViewModel.inspectionUiState.collectAsState()
    val expiringCounts = remember { mutableStateMapOf<Int, Int>() }

    LaunchedEffect(Unit) {
        inspectionViewModel.getInspections(page = 0, refresh = true)
    }

    LaunchedEffect(inspectionUiState.inspections) {
        val map = inspectionUiState.inspections
            .groupBy { it.assetId }
            .mapValues { (_, list) ->
                list.count { dto ->
                    val days = dto.expirationDate?.toString()
                        ?.let { daysUntilSomething(it.toLocalDateTime()) } ?: -1
                    days in 0..30
                }
            }
        expiringCounts.clear()
        expiringCounts.putAll(map)
    }

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(assetViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(assetUpdateUiState.success) {
        if (assetUpdateUiState.success) {
            editingAssetId = null
            assetViewModel.onAssetUpdateValueChange { it.copy(success = false) }
        }
    }

    val hasPermission =
        currentFirefighterUiState.currentFirefighter?.role.toString() != FirefighterRole.USER.toString()

    if (hasPermission) {
        LaunchedEffect(Unit) {
            assetViewModel.getAssets(page = 0, refresh = true)
            firefighterViewModel.getFirefighterByEmailAddress()

            RefreshManager.events.collect { event ->
                when (event) {
                    is RefreshEvent.AssetScreen -> assetViewModel.getAssets(
                        page = 0,
                        refresh = true
                    )

                    else -> {}
                }
            }
        }
    }

    AppListScreen(
        data = assetUiState.assets,
        isLoading = assetUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        searchPlaceholder = "Search assets...",
        filter = { asset, query ->
            val fullInfo =
                "${'$'}{asset.name} ${'$'}{asset.assetTypeName} ${'$'}{asset.description.orEmpty()}"
            query.isBlank() ||
                    asset.name.contains(query, ignoreCase = true) ||
                    asset.assetTypeName.contains(query, ignoreCase = true) ||
                    asset.description?.contains(query, ignoreCase = true) == true ||
                    fullInfo.contains(query, ignoreCase = true)
        },
        emptyText = "There aren't any assets in your VFD or the assets are still loading",
        emptyFilteredText = "No assets match your search",
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !assetUiState.isLoading) assetViewModel.getAssets(page = assetUiState.page + 1)
        },
        errorMessage = assetUiState.errorMessage,
        itemKey = { it.assetId }
    ) { asset ->
        val effectiveSelectedCode =
            assetUpdateUiState.assetType.ifBlank {
                assetTypeUiState.assetTypes
                    .firstOrNull { it.name == asset.assetTypeName }
                    ?.assetType ?: ""
            }
        if (editingAssetId == asset.assetId) {
            if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                AppAssetCard(
                    asset,
                    actions = {
                        AppTextField(
                            value = assetUpdateUiState.name,
                            onValueChange = { new ->
                                assetViewModel.onAssetUpdateValueChange {
                                    it.copy(name = new, nameTouched = true)
                                }
                            },
                            label = "Name",
                            errorMessage = assetUpdateUiState.errorMessage
                        )

                        AppDropdown(
                            items = assetTypeUiState.assetTypes,
                            selectedCode = effectiveSelectedCode,
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
                                    it.copy(description = new, descriptionTouched = true)
                                }
                            },
                            label = "Description",
                            errorMessage = assetUpdateUiState.errorMessage,
                            singleLine = false
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = "Save",
                                onClick = {
                                    asset.assetId.let { id ->
                                        val assetDto = AssetDtos.AssetPatch(
                                            name = if (assetUpdateUiState.nameTouched)
                                                assetUpdateUiState.name
                                            else null,

                                            assetType = assetUpdateUiState.assetType.takeIf { it.isNotBlank() },

                                            description = if (assetUpdateUiState.descriptionTouched)
                                                assetUpdateUiState.description
                                            else null,
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
                                val preselectedCode = assetTypeUiState.assetTypes
                                    .firstOrNull { it.name == asset.assetTypeName }
                                    ?.assetType ?: ""
                                assetViewModel.onAssetUpdateValueChange {
                                    it.copy(
                                        name = asset.name,
                                        assetType = preselectedCode,
                                        description = asset.description ?: "",
                                        nameTouched = false,
                                        assetTypeTouched = false,
                                        descriptionTouched = false,
                                    )
                                }
                            }
                        )
                        val count = expiringCounts[asset.assetId] ?: 0
                        BadgedBox(
                            badge = {
                                if (count > 0) {
                                    Badge { Text("$count") }
                                }
                            }
                        ) {
                            AppButton(
                                icon = Icons.Default.Warning,
                                label = "Inspections",
                                onClick = {
                                    val encodedName = Uri.encode(asset.name)
                                    navController.navigate("inspections/list?assetId=${asset.assetId}&assetName=$encodedName")
                                }
                            )
                        }
                    }
                }
            )
        }
    }
}