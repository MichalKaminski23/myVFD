package com.vfd.client.ui.components.dialogs

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.EventViewModel

@Composable
fun EventCreateDialog(
    eventViewModel: EventViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val eventCreateUiState = eventViewModel.eventCreateUiState.collectAsState().value

    AppUiEvents(eventViewModel.uiEvents, snackbarHostState)

    AppFormDialog(
        show = showDialog,
        onDismiss = onDismiss,
        title = stringResource(id = R.string.event_create),
        confirmEnabled = eventCreateUiState.description.isNotBlank()
                && eventCreateUiState.eventDate != null
                && eventCreateUiState.header.isNotBlank()
                && !eventCreateUiState.isLoading,
        confirmLoading = eventCreateUiState.isLoading,
        errorMessage = eventCreateUiState.errorMessage,
        onConfirm = {
            eventViewModel.createEvent(
                EventDtos.EventCreate(
                    header = eventCreateUiState.header,
                    eventDate = eventCreateUiState.eventDate!!,
                    description = eventCreateUiState.description
                )
            )
            onDismiss()
        }
    ) {
        AppTextField(
            value = eventCreateUiState.header,
            onValueChange = { new ->
                eventViewModel.onEventCreateValueChange { it.copy(header = new) }
            },
            label = stringResource(id = R.string.item_header),
            errorMessage = eventCreateUiState.errorMessage
        )

        AppDateTimePicker(
            selectedDateTime = eventCreateUiState.eventDate,
            onDateTimeSelected = { newDateTime ->
                eventViewModel.onEventCreateValueChange {
                    it.copy(eventDate = newDateTime)
                }
            },
            label = stringResource(id = R.string.item_date),
        )

        AppTextField(
            value = eventCreateUiState.description,
            onValueChange = { new ->
                eventViewModel.onEventCreateValueChange { it.copy(description = new) }
            },
            label = stringResource(id = R.string.item_description),
            errorMessage = eventCreateUiState.errorMessage,
            singleLine = false
        )
    }
}