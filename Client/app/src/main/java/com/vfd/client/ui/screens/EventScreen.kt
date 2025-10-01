package com.vfd.client.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
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
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.buttons.AppLoadMoreButton
import com.vfd.client.ui.components.cards.AppEventCard
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppSearchBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.EventViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventScreen(
    eventViewModel: EventViewModel,
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val eventUiState = eventViewModel.eventUiState.collectAsState().value
    val eventUpdateUiState = eventViewModel.eventUpdateUiState.collectAsState().value
    var editingEventId by remember { mutableStateOf<Int?>(null) }

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    val hasMore = eventUiState.page + 1 < eventUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    val filteredEvents = eventUiState.events.filter {
        searchQuery.isBlank() ||
                it.header.contains(searchQuery, ignoreCase = true)
    }

    AppUiEvents(eventViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(Unit) {
        eventViewModel.getEvents(page = 0, refresh = true)
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.EventScreen -> eventViewModel.getEvents(page = 0, refresh = true)
                else -> {}
            }
        }
    }

    LaunchedEffect(eventUpdateUiState.success) {
        if (eventUpdateUiState.success) {
            editingEventId = null
            eventViewModel.onEventUpdateValueChange { it.copy(success = false) }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search events...",
                enabled = !eventUiState.isLoading,
                loading = eventUiState.isLoading,
            )
            Spacer(Modifier.height(12.dp))
        }

        if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.USER.toString()) {
            item {
                AppText(
                    "You do not have permission to view events.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            if (filteredEvents.isEmpty()) {
                item {
                    AppText(
                        if (searchQuery.isBlank())
                            "There aren't any events in your VFD or the events are still loading"
                        else
                            "No events match your search",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            } else {
                items(filteredEvents) { event ->
                    if (editingEventId == event.eventId) {
                        if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                            AppEventCard(
                                event,
                                actions = {
                                    AppTextField(
                                        value = eventUpdateUiState.header,
                                        onValueChange = { new ->
                                            eventViewModel.onEventUpdateValueChange {
                                                it.copy(header = new)
                                            }
                                        },
                                        label = "Header",
                                        errorMessage = null
                                    )
                                    AppDateTimePicker(
                                        selectedDateTime = eventUpdateUiState.eventDate,
                                        onDateTimeSelected = { newDateTime ->
                                            eventViewModel.onEventUpdateValueChange {
                                                it.copy(eventDate = newDateTime)
                                            }
                                        }
                                    )
                                    AppTextField(
                                        value = eventUpdateUiState.description,
                                        onValueChange = { new ->
                                            eventViewModel.onEventUpdateValueChange {
                                                it.copy(description = new)
                                            }
                                        },
                                        label = "Description",
                                        errorMessage = null,
                                        singleLine = false
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        AppButton(
                                            icon = Icons.Default.Check,
                                            label = "Save",
                                            onClick = {
                                                event.eventId.let { id ->
                                                    val eventDto = EventDtos.EventPatch(
                                                        header = eventUpdateUiState.header,
                                                        eventDate = eventUpdateUiState.eventDate,
                                                        description = eventUpdateUiState.description
                                                    )
                                                    eventViewModel.updateEvent(id, eventDto)
                                                }
                                            },
                                            modifier = Modifier.weight(1f),
                                            enabled = eventUpdateUiState.header.isNotBlank() &&
                                                    eventUpdateUiState.description.isNotBlank() &&
                                                    !eventUpdateUiState.isLoading,
                                            loading = eventUpdateUiState.isLoading
                                        )
                                        AppButton(
                                            icon = Icons.Default.Close,
                                            label = "Cancel",
                                            onClick = { editingEventId = null },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            )
                            Spacer(Modifier.height(12.dp))
                        } else {
                            AppEventCard(event)
                        }
                    } else {
                        AppEventCard(
                            event,
                            actions = {
                                if (currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString()) {
                                    AppButton(
                                        icon = Icons.Default.Edit,
                                        label = "Edit",
                                        onClick = {
                                            editingEventId = event.eventId
                                            eventViewModel.onEventUpdateValueChange {
                                                it.copy(
                                                    header = event.header,
                                                    eventDate = event.eventDate,
                                                    description = event.description
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            AppLoadMoreButton(
                hasMore = hasMore,
                isLoading = eventUiState.isLoading,
                onLoadMore = {
                    if (hasMore && !eventUiState.isLoading)
                        eventViewModel.getEvents(page = eventUiState.page + 1)
                }
            )
        }

        if (eventUpdateUiState.errorMessage != null) {
            item {
                AppErrorText(eventUpdateUiState.errorMessage)
            }
        }
    }
}

