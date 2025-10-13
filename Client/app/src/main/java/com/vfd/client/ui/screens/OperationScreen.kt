package com.vfd.client.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
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
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppOperationCard
import com.vfd.client.ui.components.elements.AppAddressActions
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.elements.AppMultiDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.OperationTypeViewModel
import com.vfd.client.ui.viewmodels.OperationViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OperationScreen(
    operationViewModel: OperationViewModel,
    operationTypeViewModel: OperationTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val operationUiState by operationViewModel.operationUiState.collectAsState()
    val operationTypeUiState by operationTypeViewModel.operationTypeUiState.collectAsState()
    val operationUpdateUiState by operationViewModel.operationUpdateUiState.collectAsState()
    var editingOperationId by remember { mutableStateOf<Int?>(null) }

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = operationUiState.page + 1 < operationUiState.totalPages

    val firefighterUiState by firefighterViewModel.activeFirefightersUiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(operationViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(operationUpdateUiState.success) {
        if (operationUpdateUiState.success) {
            editingOperationId = null
            operationViewModel.onOperationUpdateValueChange { it.copy(success = false) }
        }
    }

    val hasPermission =
        currentFirefighterUiState.currentFirefighter?.role.toString() != FirefighterRole.USER.toString()

    if (hasPermission) {
        LaunchedEffect(Unit) {
            operationViewModel.getOperations(page = 0, refresh = true)
            firefighterViewModel.getFirefighterByEmailAddress()

            RefreshManager.events.collect { event ->
                when (event) {
                    is RefreshEvent.OperationScreen -> {
                        operationViewModel.getOperations(page = 0, refresh = true)
                    }

                    else -> {}
                }
            }
        }
    }

    AppListScreen(
        data = operationUiState.operations,
        isLoading = operationUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        searchPlaceholder = "Search operations...",
        filter = { operation, query ->
            query.isBlank() ||
                    operation.operationTypeName.contains(
                        query,
                        ignoreCase = true
                    ) || operation.description.contains(query, ignoreCase = true)
        },
        emptyText = "There aren't any operations in your VFD or the operations are still loading",
        emptyFilteredText = "No operations match your search",
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !operationUiState.isLoading)
                operationViewModel.getOperations(page = operationUiState.page + 1)
        },
        errorMessage = operationUiState.errorMessage,
        itemKey = { it.operationId }
    ) { operation ->
        val effectiveSelectedCode =
            operationUpdateUiState.operationType.ifBlank {
                operationTypeUiState.operationTypes
                    .firstOrNull { it.name == operation.operationTypeName }
                    ?.operationType ?: ""
            }
        if (editingOperationId == operation.operationId) {
            if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                AppOperationCard(
                    operation,
                    actions = {
                        AppDropdown(
                            items = operationTypeUiState.operationTypes,
                            selectedCode = effectiveSelectedCode,
                            codeSelector = { it.operationType },
                            labelSelector = { it.name },
                            label = "Choose operation type",
                            onSelected = { operationType ->
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(
                                        operationType = operationType.operationType,
                                        operationTypeTouched = true
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
                            selectedDateTime = operationUpdateUiState.operationDate,
                            onDateTimeSelected = { newDateTime ->
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(
                                        operationDate = newDateTime,
                                        operationDateTouched = true
                                    )
                                }
                            },
                            label = "Operation date"
                        )
                        AppDateTimePicker(
                            selectedDateTime = operationUpdateUiState.operationEnd,
                            onDateTimeSelected = { newDateTime ->
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(
                                        operationEnd = newDateTime,
                                        operationDateTouched = true
                                    )
                                }
                            },
                            label = "Operation end"
                        )
                        AppMultiDropdown(
                            items = firefighterUiState.activeFirefighters,
                            selectedIds = operationUpdateUiState.participantsIds,
                            idSelector = { it.firefighterId },
                            labelSelector = { it.firstName + " " + it.lastName },
                            label = "Participants",
                            onToggle = { id ->
                                operationViewModel.onOperationUpdateValueChange { s ->
                                    val set = s.participantsIds.toMutableSet()
                                    if (!set.add(id)) set.remove(id)
                                    s.copy(participantsIds = set, participantsTouched = true)
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
                            address = operationUpdateUiState.address,
                            errors = operationUpdateUiState.fieldErrors,
                            onAddressChange = { newAddress ->
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(address = newAddress, addressTouched = true)
                                }
                            },
                            onTouched = {
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(
                                        addressTouched = true
                                    )
                                }
                            }
                        )
                        AppTextField(
                            value = operationUpdateUiState.description,
                            onValueChange = { new ->
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(description = new, descriptionTouched = true)
                                }
                            },
                            label = "Description",
                            errorMessage = operationUpdateUiState.errorMessage,
                            singleLine = false
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = "Save",
                                onClick = {
                                    operation.operationId.let { id ->
                                        val operationDto = OperationDtos.OperationPatch(
                                            operationType = if (operationUpdateUiState.operationTypeTouched)
                                                operationUpdateUiState.operationType
                                            else null,

                                            operationDate = if (operationUpdateUiState.operationDateTouched)
                                                operationUpdateUiState.operationDate
                                            else null,

                                            operationEnd = if (operationUpdateUiState.operationDateTouched)
                                                operationUpdateUiState.operationEnd
                                            else null,

                                            description = if (operationUpdateUiState.descriptionTouched)
                                                operationUpdateUiState.description.takeIf { it.isNotBlank() }
                                            else null,

                                            address = if (operationUpdateUiState.addressTouched)
                                                operationUpdateUiState.address
                                            else null,

                                            participantsIds = if (operationUpdateUiState.participantsTouched)
                                                operationUpdateUiState.participantsIds.toSet()
                                            else null
                                        )
                                        operationViewModel.updateOperation(id, operationDto)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = operationUpdateUiState.operationDate != null &&
                                        operationUpdateUiState.operationEnd != null &&
                                        effectiveSelectedCode.isNotBlank() &&
                                        operationUpdateUiState.address?.country?.isNotBlank() == true &&
                                        operationUpdateUiState.address?.voivodeship?.isNotBlank() == true &&
                                        operationUpdateUiState.address?.city?.isNotBlank() == true &&
                                        operationUpdateUiState.address?.postalCode?.isNotBlank() == true &&
                                        operationUpdateUiState.address?.street?.isNotBlank() == true &&
                                        operationUpdateUiState.address?.houseNumber?.isNotBlank() == true &&
                                        operationUpdateUiState.description.isNotBlank() &&
                                        !operationUpdateUiState.isLoading,
                                loading = operationUpdateUiState.isLoading
                            )
                            AppButton(
                                icon = Icons.Default.Close,
                                label = "Cancel",
                                onClick = { editingOperationId = null },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
            } else {
                AppOperationCard(operation)
            }
        } else {
            AppOperationCard(
                operation,
                actions = {
                    if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                        AppButton(
                            icon = Icons.Default.Edit,
                            label = "Edit",
                            onClick = {
                                editingOperationId = operation.operationId
                                val preselectedCode = operationTypeUiState.operationTypes
                                    .firstOrNull { it.name == operation.operationTypeName }
                                    ?.operationType ?: ""
                                operationViewModel.onOperationUpdateValueChange {
                                    it.copy(
                                        operationType = preselectedCode,
                                        operationDate = operation.operationDate,
                                        operationEnd = operation.operationEnd,
                                        description = operation.description,
                                        address =
                                            AddressDtos.AddressCreate(
                                                country = operation.address.country,
                                                voivodeship = operation.address.voivodeship,
                                                city = operation.address.city,
                                                postalCode = operation.address.postalCode,
                                                street = operation.address.street,
                                                houseNumber = operation.address.houseNumber,
                                                apartNumber = operation.address.apartNumber
                                            ),
                                        participantsIds = operation.participants.map { p -> p.firefighterId }
                                            .toMutableSet(),
                                        operationTypeTouched = false,
                                        operationDateTouched = false,
                                        descriptionTouched = false,
                                        addressTouched = false,
                                        participantsTouched = false
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}