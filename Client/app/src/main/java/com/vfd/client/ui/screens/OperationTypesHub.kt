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
import com.vfd.client.data.remote.dtos.OperationTypeDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppOperationTypeCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.OperationTypeViewModel

@Composable
fun OperationTypesHub(
    operationTypeViewModel: OperationTypeViewModel
) {

    val operationTypesUiState by operationTypeViewModel.operationTypeUiState.collectAsState()
    val operationTypeCreateUiState by operationTypeViewModel.operationTypeCreateUiState.collectAsState()
    val operationTypeUpdateUiState by operationTypeViewModel.operationTypeUpdateUiState.collectAsState()

    var editingOperationTypeCode by remember { mutableStateOf<String?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(operationTypeUpdateUiState.success) {
        if (operationTypeUpdateUiState.success) {
            editingOperationTypeCode = null
            operationTypeViewModel.onOperationTypeUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        !operationTypesUiState.isLoading
    }

    LaunchedEffect(mode) {
        if (mode == "edit") {
            operationTypeViewModel.getAllOperationTypes(page = 0, size = 20)
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
                    stringResource(id = R.string.new_operation_type),
                    style = MaterialTheme.typography.headlineSmall
                )

                AppTextField(
                    value = operationTypeCreateUiState.operationType,
                    onValueChange = { new ->
                        operationTypeViewModel.onOperationTypeCreateValueChange {
                            it.copy(
                                operationType = new
                            )
                        }
                    },
                    label = stringResource(id = R.string.type_code),
                    errorMessage = operationTypeCreateUiState.errorMessage
                )

                AppTextField(
                    value = operationTypeCreateUiState.name,
                    onValueChange = { new ->
                        operationTypeViewModel.onOperationTypeCreateValueChange { it.copy(name = new) }
                    },
                    label = stringResource(id = R.string.item_name),
                    errorMessage = operationTypeCreateUiState.errorMessage
                )

                AppButton(
                    icon = Icons.Filled.Edit,
                    label = stringResource(id = R.string.save),
                    onClick = {
                        operationTypeViewModel.createOperationType(
                            operationTypeDto = OperationTypeDtos.OperationTypeCreate(
                                operationType = operationTypeCreateUiState.operationType,
                                name = operationTypeCreateUiState.name
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !operationTypeCreateUiState.isLoading && operationTypeCreateUiState.operationType.isNotBlank()
                            && operationTypeCreateUiState.name.isNotBlank(),
                    loading = operationTypeCreateUiState.isLoading

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
                        stringResource(id = R.string.edit_operation_type),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(operationTypesUiState.operationTypes) { operationType ->
                    if (editingOperationTypeCode == operationType.operationType) {
                        AppOperationTypeCard(operationType, actions = {
                            AppTextField(
                                value = operationTypeUpdateUiState.name,
                                onValueChange = { new ->
                                    operationTypeViewModel.onOperationTypeUpdateValueChange {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = stringResource(id = R.string.item_name),
                                errorMessage = operationTypeUpdateUiState.errorMessage
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = stringResource(id = R.string.save),
                                    onClick = {
                                        val operationTypeDto =
                                            OperationTypeDtos.OperationTypePatch(
                                                name = if (operationTypeUpdateUiState.nameTouched) operationTypeUpdateUiState.name else null
                                            )
                                        operationTypeViewModel.updateOperationType(
                                            operationType.operationType, operationTypeDto
                                        )
                                    },
                                    enabled = !operationTypeUpdateUiState.isLoading && operationTypeUpdateUiState.name.isNotBlank(),
                                    loading = operationTypeUpdateUiState.isLoading,
                                    modifier = Modifier.weight(1f),
                                )
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = stringResource(id = R.string.cancel),
                                    onClick = {
                                        editingOperationTypeCode = null
                                        operationTypeViewModel.onOperationTypeUpdateValueChange {
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
                        AppOperationTypeCard(operationType, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = stringResource(id = R.string.edit),
                                onClick = {
                                    editingOperationTypeCode = operationType.operationType
                                    operationTypeViewModel.onOperationTypeUpdateValueChange {
                                        it.copy(
                                            name = operationType.name,
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
                            if (!operationTypesUiState.isLoading && operationTypesUiState.page + 1 < operationTypesUiState.totalPages) {
                                operationTypeViewModel.getAllOperationTypes(
                                    page = operationTypesUiState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !operationTypesUiState.isLoading && operationTypesUiState.page + 1 < operationTypesUiState.totalPages
                    )
                }
            }
        }
    }
}