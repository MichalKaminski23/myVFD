package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.AssetTypeDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppAssetTypeCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AssetTypeViewModel

@Composable
fun AssetTypesHub(
    assetTypeViewModel: AssetTypeViewModel
) {

    val assetTypesUiState by assetTypeViewModel.assetTypeUiState.collectAsState()
    val assetTypeCreateUiState by assetTypeViewModel.assetTypeCreateUiState.collectAsState()
    val assetTypeUpdateUiState by assetTypeViewModel.assetTypeUpdateUiState.collectAsState()

    var editingAssetTypeCode by remember { mutableStateOf<String?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(assetTypeUpdateUiState.success) {
        if (assetTypeUpdateUiState.success) {
            editingAssetTypeCode = null
            assetTypeViewModel.onAssetTypeUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        !assetTypesUiState.isLoading
    }

    LaunchedEffect(mode) {
        if (mode == "edit") {
            assetTypeViewModel.getAllAssetTypes(page = 0, size = 20)
        }
    }

    AppColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (mode == "create") {
            AppColumn(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = stringResource(id = R.string.create),
                        onClick = { mode = "create" },
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = stringResource(id = R.string.edit),
                        onClick = { mode = "edit" },
                        modifier = Modifier.weight(1f)
                    )
                }
                AppText(
                    stringResource(id = R.string.new_asset_type),
                    style = MaterialTheme.typography.headlineSmall
                )

                AppTextField(
                    value = assetTypeCreateUiState.assetType,
                    onValueChange = { new ->
                        assetTypeViewModel.onAssetTypeCreateValueChange { it.copy(assetType = new) }
                    },
                    label = stringResource(id = R.string.type_code),
                    errorMessage = assetTypeCreateUiState.errorMessage
                )

                AppTextField(
                    value = assetTypeCreateUiState.name,
                    onValueChange = { new ->
                        assetTypeViewModel.onAssetTypeCreateValueChange { it.copy(name = new) }
                    },
                    label = stringResource(id = R.string.item_name),
                    errorMessage = assetTypeCreateUiState.errorMessage
                )

                AppButton(
                    icon = Icons.Filled.Edit,
                    label = stringResource(id = R.string.save),
                    onClick = {
                        assetTypeViewModel.createAssetType(
                            assetTypeDto = AssetTypeDtos.AssetTypeCreate(
                                assetType = assetTypeCreateUiState.assetType,
                                name = assetTypeCreateUiState.name
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !assetTypeCreateUiState.isLoading && assetTypeCreateUiState.assetType.isNotBlank()
                            && assetTypeCreateUiState.name.isNotBlank(),
                    loading = assetTypeCreateUiState.isLoading

                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Filled.Edit,
                            label = stringResource(id = R.string.create),
                            onClick = { mode = "create" },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            icon = Icons.Filled.Edit,
                            label = stringResource(id = R.string.edit),
                            onClick = { mode = "edit" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    AppText(
                        stringResource(id = R.string.edit_asset_type),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(assetTypesUiState.assetTypes) { assetType ->
                    if (editingAssetTypeCode == assetType.assetType) {
                        AppAssetTypeCard(assetType, actions = {
                            AppTextField(
                                value = assetTypeUpdateUiState.name,
                                onValueChange = { new ->
                                    assetTypeViewModel.onAssetTypeUpdateValueChange {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = stringResource(id = R.string.type_code),
                                errorMessage = assetTypeUpdateUiState.errorMessage
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = stringResource(id = R.string.save),
                                    onClick = {
                                        val assetTypeDto =
                                            AssetTypeDtos.AssetTypePatch(
                                                name = if (assetTypeUpdateUiState.nameTouched) assetTypeUpdateUiState.name else null
                                            )
                                        assetTypeViewModel.updateAssetType(
                                            assetType.assetType, assetTypeDto
                                        )
                                    },
                                    enabled = !assetTypeUpdateUiState.isLoading && assetTypeUpdateUiState.name.isNotBlank(),
                                    loading = assetTypeUpdateUiState.isLoading,
                                    modifier = Modifier.weight(1f),
                                )
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = stringResource(id = R.string.cancel),
                                    onClick = {
                                        editingAssetTypeCode = null
                                        assetTypeViewModel.onAssetTypeUpdateValueChange {
                                            it.copy(
                                                success = false
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        })
                    } else {
                        AppAssetTypeCard(assetType, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = stringResource(id = R.string.edit),
                                onClick = {
                                    editingAssetTypeCode = assetType.assetType
                                    assetTypeViewModel.onAssetTypeUpdateValueChange {
                                        it.copy(
                                            name = assetType.name,
                                            nameTouched = false,
                                            fieldErrors = emptyMap(),
                                            errorMessage = null,
                                            success = false,
                                            isLoading = false
                                        )
                                    }
                                }
                            )
                        })
                    }
                }
                item {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = stringResource(id = R.string.load_more),
                        onClick = {
                            if (!assetTypesUiState.isLoading && assetTypesUiState.page + 1 < assetTypesUiState.totalPages) {
                                assetTypeViewModel.getAllAssetTypes(
                                    page = assetTypesUiState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !assetTypesUiState.isLoading && assetTypesUiState.page + 1 < assetTypesUiState.totalPages
                    )
                }
            }
        }
    }
}