package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.SnackbarHostState
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
import com.vfd.client.data.remote.dtos.InspectionDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppInspectionCard
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.components.texts.AppDaysCounter
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.InspectionTypeViewModel
import com.vfd.client.ui.viewmodels.InspectionViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun InspectionScreen(
    inspectionViewModel: InspectionViewModel,
    inspectionTypeViewModel: InspectionTypeViewModel,
    firefighterViewModel: FirefighterViewModel,
    snackbarHostState: SnackbarHostState,
    assetId: Int? = null,
) {
    val inspectionUiState by inspectionViewModel.inspectionUiState.collectAsState()
    val inspectionTypeUiState by inspectionTypeViewModel.inspectionTypeUiState.collectAsState()
    val inspectionUpdateUiState by inspectionViewModel.inspectionUpdateUiState.collectAsState()
    var editingInspectionId by remember { mutableStateOf<Int?>(null) }

    val hasMore = inspectionUiState.page + 1 < inspectionUiState.totalPages

    val filteredData = remember(inspectionUiState.inspections, assetId) {
        if (assetId != null) {
            inspectionUiState.inspections.filter { it.assetId == assetId }
        } else inspectionUiState.inspections
    }

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(inspectionViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(inspectionUpdateUiState.success) {
        if (inspectionUpdateUiState.success) {
            editingInspectionId = null
            inspectionViewModel.onInspectionUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        inspectionViewModel.getInspections(page = 0, refresh = true)
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.InspectionScreen -> {
                    inspectionViewModel.getInspections(page = 0, refresh = true)
                }

                else -> {}
            }
        }
    }
    AppListScreen(
        data = filteredData,
        isLoading = inspectionUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        filter = { inspection, query ->
            query.isBlank() ||
                    inspection.inspectionTypeName.contains(
                        query,
                        ignoreCase = true
                    )
        },
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !inspectionUiState.isLoading)
                inspectionViewModel.getInspections(page = inspectionUiState.page + 1)
        },
        errorMessage = inspectionUiState.errorMessage,
        itemKey = { it.inspectionId }
    ) { inspection ->
        val effectiveSelectedCode =
            inspectionUpdateUiState.inspectionType.ifBlank {
                inspectionTypeUiState.inspectionTypes
                    .firstOrNull { it.name == inspection.inspectionTypeName }
                    ?.inspectionType ?: ""
            }
        if (editingInspectionId == inspection.inspectionId) {
            AppInspectionCard(
                inspection,
                actions = {
                    AppDropdown(
                        items = inspectionTypeUiState.inspectionTypes,
                        selectedCode = effectiveSelectedCode,
                        codeSelector = { it.inspectionType },
                        labelSelector = { it.name },
                        label = stringResource(id = R.string.item_type),
                        onSelected = { inspectionType ->
                            inspectionViewModel.onInspectionUpdateValueChange {
                                it.copy(
                                    inspectionType = inspectionType.inspectionType,
                                    inspectionTypeTouched = true
                                )
                            }
                        },
                        onLoadMore = {
                            if (inspectionTypeUiState.page + 1 < inspectionTypeUiState.totalPages) {
                                inspectionTypeViewModel.getAllInspectionTypes(
                                    page = inspectionTypeUiState.page + 1
                                )
                            }
                        },
                        hasMore = inspectionTypeUiState.page + 1 < inspectionTypeUiState.totalPages,
                        onExpand = {
                            if (inspectionTypeUiState.inspectionTypes.isEmpty())
                                inspectionTypeViewModel.getAllInspectionTypes(page = 0)
                        },
                        icon = Icons.Default.Build
                    )
                    AppDateTimePicker(
                        selectedDateTime = inspectionUpdateUiState.inspectionDate,
                        onDateTimeSelected = { newDateTime ->
                            inspectionViewModel.onInspectionUpdateValueChange {
                                it.copy(
                                    inspectionDate = newDateTime,
                                    inspectionDateTouched = true
                                )
                            }
                        },
                        label = stringResource(id = R.string.item_date),
                    )
                    AppDateTimePicker(
                        selectedDateTime = inspectionUpdateUiState.expirationDate,
                        onDateTimeSelected = { newDateTime ->
                            inspectionViewModel.onInspectionUpdateValueChange {
                                it.copy(
                                    expirationDate = newDateTime,
                                    expirationDateTouched = true
                                )
                            }
                        },
                        label = stringResource(id = R.string.item_end_date),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Default.Check,
                            label = stringResource(id = R.string.save),
                            onClick = {
                                inspection.inspectionId.let { id ->
                                    val inspectionDto = InspectionDtos.InspectionPatch(
                                        inspectionType = if (inspectionUpdateUiState.inspectionTypeTouched)
                                            inspectionUpdateUiState.inspectionType
                                        else null,

                                        inspectionDate = if (inspectionUpdateUiState.inspectionDateTouched)
                                            inspectionUpdateUiState.inspectionDate
                                        else null,

                                        expirationDate = if (inspectionUpdateUiState.expirationDateTouched)
                                            inspectionUpdateUiState.expirationDate
                                        else null,
                                    )
                                    inspectionViewModel.updateInspection(id, inspectionDto)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = inspectionUpdateUiState.inspectionDate != null &&
                                    effectiveSelectedCode.isNotBlank() &&
                                    inspectionUpdateUiState.expirationDate != null &&
                                    !inspectionUpdateUiState.isLoading,
                            loading = inspectionUpdateUiState.isLoading
                        )
                        AppButton(
                            icon = Icons.Default.Close,
                            label = stringResource(id = R.string.cancel),
                            onClick = { editingInspectionId = null },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            )
            AppErrorText(
                message = inspectionUpdateUiState.errorMessage ?: ""
            )
        } else {
            AppInspectionCard(
                inspection,
                actions = {
                    AppButton(
                        icon = Icons.Default.Edit,
                        label = stringResource(id = R.string.edit),
                        onClick = {
                            editingInspectionId = inspection.inspectionId
                            val preselectedCode = inspectionTypeUiState.inspectionTypes
                                .firstOrNull { it.name == inspection.inspectionTypeName }
                                ?.inspectionType ?: ""
                            inspectionViewModel.onInspectionUpdateValueChange {
                                it.copy(
                                    inspectionType = preselectedCode,
                                    inspectionDate = inspection.inspectionDate,
                                    expirationDate = inspection.expirationDate,
                                    inspectionTypeTouched = false,
                                    inspectionDateTouched = false,
                                    expirationDateTouched = false,
                                )
                            }
                        }
                    )
                    AppDaysCounter(
                        ourDate = inspection.expirationDate
                    )
                }
            )
        }
    }
}