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
import androidx.compose.ui.unit.dp
import com.vfd.client.data.remote.dtos.InspectionTypeDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppInspectionTypeCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.InspectionTypeViewModel

@Composable
fun InspectionTypesHub(
    inspectionTypeViewModel: InspectionTypeViewModel
) {

    val inspectionTypesUiState by inspectionTypeViewModel.inspectionTypeUiState.collectAsState()
    val inspectionTypeCreateUiState by inspectionTypeViewModel.inspectionTypeCreateUiState.collectAsState()
    val inspectionTypeUpdateUiState by inspectionTypeViewModel.inspectionTypeUpdateUiState.collectAsState()

    var editingInspectionTypeCode by remember { mutableStateOf<String?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(inspectionTypeUpdateUiState.success) {
        if (inspectionTypeUpdateUiState.success) {
            editingInspectionTypeCode = null
            inspectionTypeViewModel.onInspectionTypeUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        !inspectionTypesUiState.isLoading
    }

    LaunchedEffect(mode) {
        if (mode == "edit") {
            inspectionTypeViewModel.getAllInspectionTypes(page = 0, size = 20)
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
                        label = "Create",
                        onClick = { mode = "create" },
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Edit",
                        onClick = { mode = "edit" },
                        modifier = Modifier.weight(1f)
                    )
                }
                AppText("New inspection type", style = MaterialTheme.typography.headlineSmall)

                AppTextField(
                    value = inspectionTypeCreateUiState.inspectionType,
                    onValueChange = { new ->
                        inspectionTypeViewModel.onInspectionTypeCreateValueChange {
                            it.copy(
                                inspectionType = new
                            )
                        }
                    },
                    label = "Type",
                    errorMessage = inspectionTypeCreateUiState.errorMessage
                )

                AppTextField(
                    value = inspectionTypeCreateUiState.name,
                    onValueChange = { new ->
                        inspectionTypeViewModel.onInspectionTypeCreateValueChange { it.copy(name = new) }
                    },
                    label = "Name",
                    errorMessage = inspectionTypeCreateUiState.errorMessage
                )

                AppButton(
                    icon = Icons.Filled.Edit,
                    label = "Save",
                    onClick = {
                        inspectionTypeViewModel.createInspectionType(
                            inspectionTypeDto = InspectionTypeDtos.InspectionTypeCreate(
                                inspectionType = inspectionTypeCreateUiState.inspectionType,
                                name = inspectionTypeCreateUiState.name
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !inspectionTypeCreateUiState.isLoading && inspectionTypeCreateUiState.inspectionType.isNotBlank()
                            && inspectionTypeCreateUiState.name.isNotBlank(),
                    loading = inspectionTypeCreateUiState.isLoading
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
                            label = "Create",
                            onClick = { mode = "create" },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            icon = Icons.Filled.Edit,
                            label = "Edit",
                            onClick = { mode = "edit" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    AppText("Edit inspection type", style = MaterialTheme.typography.headlineSmall)
                }

                items(inspectionTypesUiState.inspectionTypes) { inspectionType ->
                    if (editingInspectionTypeCode == inspectionType.inspectionType) {
                        AppInspectionTypeCard(inspectionType, actions = {
                            AppTextField(
                                value = inspectionTypeUpdateUiState.name,
                                onValueChange = { new ->
                                    inspectionTypeViewModel.onInspectionTypeUpdateValueChange() {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = "Name",
                                errorMessage = inspectionTypeUpdateUiState.errorMessage
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Save",
                                    onClick = {
                                        val inspectionTypeDto =
                                            InspectionTypeDtos.InspectionTypePatch(
                                                name = if (inspectionTypeUpdateUiState.nameTouched) inspectionTypeUpdateUiState.name else null
                                            )
                                        inspectionTypeViewModel.updateInspectionType(
                                            inspectionType.inspectionType, inspectionTypeDto
                                        )
                                    },
                                    enabled = !inspectionTypeUpdateUiState.isLoading && inspectionTypeUpdateUiState.name.isNotBlank(),
                                    loading = inspectionTypeUpdateUiState.isLoading,
                                    modifier = Modifier.weight(1f),
                                )
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Cancel",
                                    onClick = {
                                        editingInspectionTypeCode = null
                                        inspectionTypeViewModel.onInspectionTypeUpdateValueChange {
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
                        AppInspectionTypeCard(inspectionType, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = "Edit",
                                onClick = {
                                    editingInspectionTypeCode = inspectionType.inspectionType
                                    inspectionTypeViewModel.onInspectionTypeUpdateValueChange {
                                        it.copy(
                                            name = inspectionType.name,
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
                        label = "Load more",
                        onClick = {
                            if (!inspectionTypesUiState.isLoading && inspectionTypesUiState.page + 1 < inspectionTypesUiState.totalPages) {
                                inspectionTypeViewModel.getAllInspectionTypes(
                                    page = inspectionTypesUiState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !inspectionTypesUiState.isLoading && inspectionTypesUiState.page + 1 < inspectionTypesUiState.totalPages
                    )
                }
            }
        }
    }
}