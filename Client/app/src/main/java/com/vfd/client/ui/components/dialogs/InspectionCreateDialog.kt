package com.vfd.client.ui.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.InspectionDtos
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.viewmodels.InspectionTypeViewModel
import com.vfd.client.ui.viewmodels.InspectionViewModel

@Composable
fun InspectionCreateDialog(
    inspectionViewModel: InspectionViewModel,
    inspectionTypeViewModel: InspectionTypeViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    assetId: Int? = null
) {
    val inspectionCreateUiState = inspectionViewModel.inspectionCreateUiState.collectAsState().value
    val inspectionTypeUiState by inspectionTypeViewModel.inspectionTypeUiState.collectAsState()

    LaunchedEffect(showDialog, assetId) {
        if (showDialog && (assetId ?: -1) > 0) {
            inspectionViewModel.onInspectionCreateValueChange {
                it.copy(assetId = assetId!!)
            }
        }
    }

    AppUiEvents(inspectionViewModel.uiEvents, snackbarHostState)

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = stringResource(id = R.string.inspection_create),
        confirmEnabled = inspectionCreateUiState.assetId > 0 &&
                inspectionCreateUiState.inspectionType.isNotBlank()
                && inspectionCreateUiState.inspectionDate != null
                && !inspectionCreateUiState.isLoading,
        confirmLoading = inspectionCreateUiState.isLoading,
        errorMessage = inspectionCreateUiState.errorMessage,
        onConfirm = {
            inspectionViewModel.createInspection(
                InspectionDtos.InspectionCreate(
                    assetId = inspectionCreateUiState.assetId,
                    inspectionType = inspectionCreateUiState.inspectionType,
                    inspectionDate = inspectionCreateUiState.inspectionDate!!,
                    expirationDate = inspectionCreateUiState.expirationDate
                )
            )
            onDismiss()
        }
    ) {
        AppDropdown(
            items = inspectionTypeUiState.inspectionTypes,
            selectedCode = inspectionCreateUiState.inspectionType,
            codeSelector = { it.inspectionType },
            labelSelector = { it.name },
            label = stringResource(id = R.string.item_type),
            onSelected = { inspectionType ->
                inspectionViewModel.onInspectionCreateValueChange {
                    it.copy(
                        inspectionType = inspectionType.inspectionType
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
            selectedDateTime = inspectionCreateUiState.inspectionDate,
            onDateTimeSelected = { newDateTime ->
                inspectionViewModel.onInspectionCreateValueChange {
                    it.copy(
                        inspectionDate = newDateTime
                    )
                }
            },
            label = stringResource(id = R.string.item_date),
        )
        AppDateTimePicker(
            selectedDateTime = inspectionCreateUiState.expirationDate,
            onDateTimeSelected = { newDateTime ->
                inspectionViewModel.onInspectionCreateValueChange {
                    it.copy(
                        expirationDate = newDateTime
                    )
                }
            },
            label = stringResource(id = R.string.item_end_date),
        )
    }
}