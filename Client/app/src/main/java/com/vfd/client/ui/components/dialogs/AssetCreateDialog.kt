package com.vfd.client.ui.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AssetViewModel

@Composable
fun AssetCreateDialog(
    assetViewModel: AssetViewModel,
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val assetCreateUiState = assetViewModel.assetCreateUiState.collectAsState().value
    val assetTypeUiState = assetTypeViewModel.assetTypeUiState.collectAsState().value

    AppUiEvents(assetViewModel.uiEvents, snackbarHostState)

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = stringResource(id = R.string.asset_create),
        confirmEnabled = assetCreateUiState.name.isNotBlank()
                && assetCreateUiState.assetType.isNotBlank()
                && !assetCreateUiState.isLoading,
        confirmLoading = assetCreateUiState.isLoading,
        errorMessage = assetCreateUiState.errorMessage,
        onConfirm = {
            assetViewModel.createAsset(
                AssetDtos.AssetCreate(
                    name = assetCreateUiState.name,
                    assetType = assetCreateUiState.assetType,
                    description = assetCreateUiState.description
                )
            )
            onDismiss()
        }
    ) {
        AppTextField(
            value = assetCreateUiState.name,
            onValueChange = { new ->
                assetViewModel.onAssetCreateValueChange { it.copy(name = new) }
            },
            label = stringResource(id = R.string.item_name),
            errorMessage = assetCreateUiState.errorMessage
        )

        AppDropdown(
            items = assetTypeUiState.assetTypes,
            selectedCode = assetCreateUiState.assetType,
            codeSelector = { it.assetType },
            labelSelector = { it.name },
            label = stringResource(id = R.string.item_type),
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
            label = stringResource(id = R.string.item_description),
            errorMessage = assetCreateUiState.errorMessage,
            singleLine = false
        )
    }
}