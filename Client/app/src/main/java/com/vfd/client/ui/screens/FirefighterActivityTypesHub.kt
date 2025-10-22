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
import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterActivityTypeCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterActivityTypeViewModel

@Composable
fun FirefighterActivityTypesHub(
    firefighterActivityTypeViewModel: FirefighterActivityTypeViewModel
) {

    val firefighterActivityTypesUiState by firefighterActivityTypeViewModel.firefighterActivityTypeUiState.collectAsState()
    val firefighterActivityTypeCreateUiState by firefighterActivityTypeViewModel.firefighterActivityTypeCreateUiState.collectAsState()
    val firefighterActivityTypeUpdateUiState by firefighterActivityTypeViewModel.firefighterActivityTypeUpdateUiState.collectAsState()

    var editingFirefighterActivityTypeCode by remember { mutableStateOf<String?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(firefighterActivityTypeUpdateUiState.success) {
        if (firefighterActivityTypeUpdateUiState.success) {
            editingFirefighterActivityTypeCode = null
            firefighterActivityTypeViewModel.onFirefighterActivityTypeUpdateValueChange {
                it.copy(
                    success = false
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        !firefighterActivityTypesUiState.isLoading
    }

    LaunchedEffect(mode) {
        if (mode == "edit") {
            firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(page = 0, size = 20)
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
                AppText(
                    "New firefighter activity type",
                    style = MaterialTheme.typography.headlineSmall
                )

                AppTextField(
                    value = firefighterActivityTypeCreateUiState.firefighterActivityType,
                    onValueChange = { new ->
                        firefighterActivityTypeViewModel.onFirefighterActivityTypeCreateValueChange {
                            it.copy(
                                firefighterActivityType = new
                            )
                        }
                    },
                    label = "Type",
                    errorMessage = firefighterActivityTypeCreateUiState.errorMessage
                )

                AppTextField(
                    value = firefighterActivityTypeCreateUiState.name,
                    onValueChange = { new ->
                        firefighterActivityTypeViewModel.onFirefighterActivityTypeCreateValueChange {
                            it.copy(
                                name = new
                            )
                        }
                    },
                    label = "Name",
                    errorMessage = firefighterActivityTypeCreateUiState.errorMessage
                )

                AppButton(
                    icon = Icons.Filled.Edit,
                    label = "Save",
                    onClick = {
                        firefighterActivityTypeViewModel.createFirefighterActivityType(
                            firefighterActivityTypeDto = FirefighterActivityTypeDtos.FirefighterActivityTypeCreate(
                                firefighterActivityType = firefighterActivityTypeCreateUiState.firefighterActivityType,
                                name = firefighterActivityTypeCreateUiState.name
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !firefighterActivityTypeCreateUiState.isLoading && firefighterActivityTypeCreateUiState.firefighterActivityType.isNotBlank()
                            && firefighterActivityTypeCreateUiState.name.isNotBlank(),
                    loading = firefighterActivityTypeCreateUiState.isLoading

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
                    AppText(
                        "Edit firefighter activity type",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                items(firefighterActivityTypesUiState.firefighterActivityTypes) { firefighterActivityType ->
                    if (editingFirefighterActivityTypeCode == firefighterActivityType.firefighterActivityType) {
                        AppFirefighterActivityTypeCard(firefighterActivityType, actions = {
                            AppTextField(
                                value = firefighterActivityTypeUpdateUiState.name,
                                onValueChange = { new ->
                                    firefighterActivityTypeViewModel.onFirefighterActivityTypeUpdateValueChange() {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = "Name",
                                errorMessage = firefighterActivityTypeUpdateUiState.errorMessage
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Save",
                                    onClick = {
                                        val firefighterActivityTypeDto =
                                            FirefighterActivityTypeDtos.FirefighterActivityTypePatch(
                                                name = if (firefighterActivityTypeUpdateUiState.nameTouched) firefighterActivityTypeUpdateUiState.name else null
                                            )
                                        firefighterActivityTypeViewModel.updateFirefighterActivityType(
                                            firefighterActivityType.firefighterActivityType,
                                            firefighterActivityTypeDto
                                        )
                                    },
                                    enabled = !firefighterActivityTypeUpdateUiState.isLoading && firefighterActivityTypeUpdateUiState.name.isNotBlank(),
                                    loading = firefighterActivityTypeUpdateUiState.isLoading,
                                    modifier = Modifier.weight(1f),
                                )
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Cancel",
                                    onClick = {
                                        editingFirefighterActivityTypeCode = null
                                        firefighterActivityTypeViewModel.onFirefighterActivityTypeUpdateValueChange {
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
                        AppFirefighterActivityTypeCard(firefighterActivityType, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = "Edit",
                                onClick = {
                                    editingFirefighterActivityTypeCode =
                                        firefighterActivityType.firefighterActivityType
                                    firefighterActivityTypeViewModel.onFirefighterActivityTypeUpdateValueChange {
                                        it.copy(
                                            name = firefighterActivityType.name,
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
                            if (!firefighterActivityTypesUiState.isLoading && firefighterActivityTypesUiState.page + 1 < firefighterActivityTypesUiState.totalPages) {
                                firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(
                                    page = firefighterActivityTypesUiState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !firefighterActivityTypesUiState.isLoading && firefighterActivityTypesUiState.page + 1 < firefighterActivityTypesUiState.totalPages
                    )
                }
            }
        }
    }
}