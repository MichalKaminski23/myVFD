package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppCard
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppDropdown
import com.vfd.client.ui.components.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel

@Composable
fun AssetScreen(
    assetViewModel: AssetViewModel = hiltViewModel(),
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    navController: NavController
) {

    val assetsFromMyFiredepartment by assetViewModel.assetsFromMyDepartment.collectAsState()

    val assetTypeUiState by assetTypeViewModel.assetTypeUiState.collectAsState()

    val assetUpdateUiState by assetViewModel.assetUpdateUiState.collectAsState()
    var editingAssetId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        assetViewModel.getAssetsFromMyFiredepartment()
    }

    LaunchedEffect(assetUpdateUiState.success) {
        if (assetUpdateUiState.success) {
            editingAssetId = null
            assetViewModel.onAssetUpdateValueChange { it.copy(success = false) }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (assetsFromMyFiredepartment.isEmpty()) {
            Text(
                "There aren't any assets in your VFD",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            assetsFromMyFiredepartment.forEach { asset ->
                if (editingAssetId == asset.assetId) {
                    AppCard(
                        header = "\uD83E\uDE93 Editing: ${asset.name}",
                        smallerHeader = "\uD83D\uDEA8 Type: ${asset.assetTypeName}",
                        otherText = "✏\uFE0F Description: ${asset.description}",
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
                                errorMessage = null,
                                redBackground = true
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
                                singleLine = false,
                                redBackground = true
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
                            {
                                AppButton(
                                    icon = Icons.Default.Check,
                                    label = "Save",
                                    onClick = {
                                        asset.assetId.let {
                                            assetViewModel.updateAsset(
                                                it,
                                                assetUpdateUiState.name,
                                                assetUpdateUiState.assetType,
                                                assetUpdateUiState.description
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = assetUpdateUiState.name.isNotBlank() && assetUpdateUiState.description.isNotBlank()
                                            && !assetUpdateUiState.loading,
                                    loading = assetUpdateUiState.loading
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
                    AppCard(
                        "\uD83E\uDE93 ${asset.name}",
                        "\uD83D\uDEA8 Type: ${asset.assetTypeName}",
                        "✏\uFE0F Description: ${asset.description}",
                        {
                            AppButton(
                                icon = Icons.Default.Edit,
                                label = "Edit",
                                onClick = {
                                    editingAssetId = asset.assetId
                                    assetViewModel.onAssetUpdateValueChange {
                                        it.copy(
                                            name = asset.name,
                                            assetType = "",
                                            description = asset.description
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }

        if (assetUpdateUiState.error != null) {
            Text(
                text = assetUpdateUiState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}