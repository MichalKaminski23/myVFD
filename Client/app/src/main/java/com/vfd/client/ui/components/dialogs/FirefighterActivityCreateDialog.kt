package com.vfd.client.ui.components.dialogs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterActivityTypeViewModel
import com.vfd.client.ui.viewmodels.FirefighterActivityViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FirefighterActivityCreateDialog(
    firefighterActivityViewModel: FirefighterActivityViewModel,
    firefighterActivityTypeViewModel: FirefighterActivityTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    firefighterId: Int? = null
) {

    val firefighterActivityCreateUiState =
        firefighterActivityViewModel.firefighterActivityCreateUiState.collectAsState().value
    val firefighterActivityTypeUiState by firefighterActivityTypeViewModel.firefighterActivityTypeUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    AppUiEvents(firefighterActivityViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(showDialog, firefighterId) {
        if (showDialog && (firefighterId ?: -1) > 0) {
            firefighterActivityViewModel.onActivityCreateValueChange() {
                it.copy(firefighterId = firefighterId!!)
            }
        }
    }

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = "Create new activity",
        confirmEnabled =
            firefighterActivityCreateUiState.activityType.isNotBlank() &&
                    firefighterActivityCreateUiState.activityDate != null &&
                    firefighterActivityCreateUiState.expirationDate != null
                    && !firefighterActivityCreateUiState.isLoading,
        confirmLoading = firefighterActivityCreateUiState.isLoading,
        errorMessage = firefighterActivityCreateUiState.errorMessage,
        onConfirm = {
            firefighterActivityViewModel.createFirefighterActivity(
                FirefighterActivityDtos.FirefighterActivityCreate(
                    firefighterActivityType = firefighterActivityCreateUiState.activityType,
                    activityDate = firefighterActivityCreateUiState.activityDate!!,
                    expirationDate = firefighterActivityCreateUiState.expirationDate!!,
                    description = firefighterActivityCreateUiState.description
                )
            )
            onDismiss()
        }
    ) {
        AppDropdown(
            items = firefighterActivityTypeUiState.firefighterActivityTypes,
            selectedCode = firefighterActivityCreateUiState.activityType,
            codeSelector = { it.firefighterActivityType },
            labelSelector = { it.name },
            label = "Choose activity type",
            onSelected = { firefighterActivityType ->
                firefighterActivityViewModel.onActivityCreateValueChange() {
                    it.copy(
                        activityType = firefighterActivityType.firefighterActivityType
                    )
                }
            },
            onLoadMore = {
                if (firefighterActivityTypeUiState.page + 1 < firefighterActivityTypeUiState.totalPages) {
                    firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(
                        page = firefighterActivityTypeUiState.page + 1
                    )
                }
            },
            hasMore = firefighterActivityTypeUiState.page + 1 < firefighterActivityTypeUiState.totalPages,
            onExpand = {
                if (firefighterActivityTypeUiState.firefighterActivityTypes.isEmpty())
                    firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(
                        page = 0
                    )
            },
            icon = Icons.Default.Build
        )
        AppDateTimePicker(
            selectedDateTime = firefighterActivityCreateUiState.activityDate,
            onDateTimeSelected = { newDateTime ->
                firefighterActivityViewModel.onActivityCreateValueChange {
                    it.copy(
                        activityDate = newDateTime
                    )
                }
            },
            label = "Activity date"
        )
        AppDateTimePicker(
            selectedDateTime = firefighterActivityCreateUiState.expirationDate,
            onDateTimeSelected = { newDateTime ->
                firefighterActivityViewModel.onActivityCreateValueChange {
                    it.copy(
                        expirationDate = newDateTime
                    )
                }
            },
            label = "Expiration date"
        )
        AppTextField(
            value = firefighterActivityCreateUiState.description,
            onValueChange = { new ->
                firefighterActivityViewModel.onActivityCreateValueChange() {
                    it.copy(description = new)
                }
            },
            label = "Description",
            errorMessage = firefighterActivityCreateUiState.errorMessage,
            singleLine = false
        )
    }
}