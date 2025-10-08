package com.vfd.client.ui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.ui.components.elements.AppAddressActions
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.elements.AppMultiDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.OperationTypeViewModel
import com.vfd.client.ui.viewmodels.OperationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OperationCreateDialog(
    operationViewModel: OperationViewModel,
    operationTypeViewModel: OperationTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val operationCreateUiState = operationViewModel.operationCreateUiState.collectAsState().value
    val operationTypeUiState = operationTypeViewModel.operationTypeUiState.collectAsState().value

    val firefighterUiState by firefighterViewModel.activeFirefightersUiState.collectAsState()

    AppUiEvents(operationViewModel.uiEvents, snackbarHostState)

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = "Create new operation",
        confirmEnabled = operationCreateUiState.operationType.isNotBlank()
                && operationCreateUiState.address?.country?.isNotBlank() == true &&
                operationCreateUiState.address?.voivodeship?.isNotBlank() == true &&
                operationCreateUiState.address?.city?.isNotBlank() == true &&
                operationCreateUiState.address?.postalCode?.isNotBlank() == true &&
                operationCreateUiState.address?.street?.isNotBlank() == true &&
                operationCreateUiState.address?.houseNumber?.isNotBlank() == true &&
                operationCreateUiState.description.isNotBlank() &&
                operationCreateUiState.operationDate != null &&
                operationCreateUiState.description.isNotBlank() &&
                operationCreateUiState.participantsIds != null
                && !operationCreateUiState.isLoading,
        confirmLoading = operationCreateUiState.isLoading,
        errorMessage = operationCreateUiState.errorMessage,
        onConfirm = {
            operationViewModel.createOperation(
                OperationDtos.OperationCreate(
                    operationType = operationCreateUiState.operationType,
                    address = operationCreateUiState.address!!,
                    operationDate = operationCreateUiState.operationDate!!,
                    description = operationCreateUiState.description,
                    participantsIds = operationCreateUiState.participantsIds!!
                )
            )
            onDismiss()
        }
    ) {
        AppDropdown(
            items = operationTypeUiState.operationTypes,
            selectedCode = operationCreateUiState.operationType,
            codeSelector = { it.operationType },
            labelSelector = { it.name },
            label = "Choose operation type",
            onSelected = { operationType ->
                operationViewModel.onOperationCreateValueChange {
                    it.copy(
                        operationType = operationType.operationType
                    )
                }
            },
            onLoadMore = {
                if (operationTypeUiState.page + 1 < operationTypeUiState.totalPages) {
                    operationTypeViewModel.getAllOperationTypes(
                        page = operationTypeUiState.page + 1
                    )
                }
            },
            hasMore = operationTypeUiState.page + 1 < operationTypeUiState.totalPages,
            onExpand = {
                if (operationTypeUiState.operationTypes.isEmpty())
                    operationTypeViewModel.getAllOperationTypes(page = 0)
            },
            icon = Icons.Default.Build
        )
        AppDateTimePicker(
            selectedDateTime = operationCreateUiState.operationDate,
            onDateTimeSelected = { newDateTime ->
                operationViewModel.onOperationCreateValueChange {
                    it.copy(
                        operationDate = newDateTime
                    )
                }
            }
        )
        AppMultiDropdown(
            items = firefighterUiState.activeFirefighters,
            selectedIds = operationCreateUiState.participantsIds,
            idSelector = { it.firefighterId },
            labelSelector = { it.firstName + " " + it.lastName },
            label = "Participants",
            onToggle = { id ->
                operationViewModel.onOperationCreateValueChange { s ->
                    val set = s.participantsIds.toMutableSet()
                    if (!set.add(id)) set.remove(id)
                    s.copy(participantsIds = set)
                }
            },
            onExpand = {
                if (firefighterUiState.activeFirefighters.isEmpty()) {
                    firefighterViewModel.getFirefighters(page = 0)
                }
            },
            onLoadMore = { firefighterViewModel.getFirefighters(page = firefighterUiState.page + 1) },
            hasMore = firefighterUiState.page + 1 < firefighterUiState.totalPages,
            icon = Icons.Default.Face,
            isLoading = firefighterUiState.isLoading
        )
        AppAddressActions(
            address = operationCreateUiState.address,
            errors = operationCreateUiState.fieldErrors,
            onAddressChange = { newAddress ->
                operationViewModel.onOperationCreateValueChange {
                    it.copy(address = newAddress)
                }
            }
        )
        AppTextField(
            value = operationCreateUiState.description,
            onValueChange = { new ->
                operationViewModel.onOperationCreateValueChange {
                    it.copy(description = new)
                }
            },
            label = "Description",
            errorMessage = operationCreateUiState.errorMessage,
            singleLine = false
        )
    }
}