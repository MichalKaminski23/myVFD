package com.vfd.client.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.EventViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCreateDialog(
    eventViewModel: EventViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val eventCreateUiState = eventViewModel.eventCreateUiState.collectAsState().value

    AppUiEvents(eventViewModel.uiEvents, snackbarHostState)

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                )
            ) {
                AppColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    AppText(
                        "Create new event",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(12.dp))

                    AppTextField(
                        value = eventCreateUiState.header,
                        onValueChange = { new ->
                            eventViewModel.onEventCreateValueChange { it.copy(header = new) }
                        },
                        label = "Header",
                        errorMessage = null
                    )

                    AppDateTimePicker(
                        selectedDateTime = eventCreateUiState.eventDate,
                        onDateTimeSelected = { newDateTime ->
                            eventViewModel.onEventCreateValueChange {
                                it.copy(eventDate = newDateTime)
                            }
                        }
                    )

                    AppTextField(
                        value = eventCreateUiState.description,
                        onValueChange = { new ->
                            eventViewModel.onEventCreateValueChange { it.copy(description = new) }
                        },
                        label = "Description",
                        errorMessage = null,
                        singleLine = false
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Default.Check,
                            label = "Save",
                            enabled = eventCreateUiState.description.isNotBlank()
                                    && eventCreateUiState.eventDate != null
                                    && eventCreateUiState.header.isNotBlank()
                                    && !eventCreateUiState.isLoading,
                            loading = eventCreateUiState.isLoading,
                            onClick = {
                                eventViewModel.createEvent(
                                    EventDtos.EventCreate(
                                        header = eventCreateUiState.header,
                                        eventDate = eventCreateUiState.eventDate!!,
                                        description = eventCreateUiState.description
                                    )
                                )
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            icon = Icons.Default.Close,
                            label = "Cancel",
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (eventCreateUiState.errorMessage != null) {
                        AppErrorText(eventCreateUiState.errorMessage)
                    }
                }
            }
        }
    }
}