package com.vfd.client.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel

@Composable
fun AssetCreateDialog(
    assetViewModel: AssetViewModel,
    assetTypeViewModel: AssetTypeViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val assetCreateUiState = assetViewModel.assetCreateUiState.collectAsState().value
    val assetTypeUiState = assetTypeViewModel.assetTypeUiState.collectAsState().value

    AppUiEvents(assetViewModel.uiEvents, snackbarHostState)

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                )
            ) {
                AppColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AppText(
                        "Create new asset",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(12.dp))

                    AppTextField(
                        value = assetCreateUiState.name,
                        onValueChange = { new ->
                            assetViewModel.onAssetCreateValueChange { it.copy(name = new) }
                        },
                        label = "Name",
                        errorMessage = null
                    )

                    AppDropdown(
                        items = assetTypeUiState.assetTypes,
                        selectedCode = assetCreateUiState.assetType,
                        codeSelector = { it.assetType },
                        labelSelector = { it.name },
                        label = "Choose asset type",
                        onSelected = { type ->
                            assetViewModel.onAssetCreateValueChange { it.copy(assetType = type.assetType) }
                        },
                        onExpand = {
                            if (assetTypeUiState.assetTypes.isEmpty())
                                assetTypeViewModel.getAllAssetTypes(page = 0)
                        },
                        hasMore = assetTypeUiState.page + 1 < assetTypeUiState.totalPages,
                        onLoadMore = {
                            assetTypeViewModel.getAllAssetTypes(page = assetTypeUiState.page + 1)
                        },
                        icon = Icons.Default.Build
                    )

                    AppTextField(
                        value = assetCreateUiState.description,
                        onValueChange = { new ->
                            assetViewModel.onAssetCreateValueChange { it.copy(description = new) }
                        },
                        label = "Description",
                        errorMessage = null,
                        singleLine = false
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Default.Check,
                            label = "Save",
                            enabled = assetCreateUiState.name.isNotBlank()
                                    && assetCreateUiState.assetType.isNotBlank()
                                    && !assetCreateUiState.isLoading,
                            loading = assetCreateUiState.isLoading,
                            onClick = {
                                assetViewModel.createAsset(
                                    AssetDtos.AssetCreate(
                                        name = assetCreateUiState.name,
                                        assetType = assetCreateUiState.assetType,
                                        description = assetCreateUiState.description
                                    )
                                )
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            icon = Icons.Default.Close,
                            label = "Cancel",
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (assetCreateUiState.errorMessage != null) {
                        AppErrorText(assetCreateUiState.errorMessage)
                    }
                }
            }
        }
    }
}
