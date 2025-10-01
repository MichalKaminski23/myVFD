package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.data.repositories.EventRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

data class EventUiState(
    val events: List<EventDtos.EventResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class EventUpdateUiState(
    val header: String = "",
    val description: String = "",
    val eventDate: LocalDateTime? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

data class EventCreateUiState(
    val header: String = "",
    val description: String = "",
    val eventDate: LocalDateTime? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _eventUiState = MutableStateFlow(EventUiState())
    val eventUiState = _eventUiState.asStateFlow()

    private val _eventUpdateUiState = MutableStateFlow(EventUpdateUiState())
    val eventUpdateUiState = _eventUpdateUiState.asStateFlow()

    private val _eventCreateUiState = MutableStateFlow(EventCreateUiState())
    val eventCreateUiState = _eventCreateUiState.asStateFlow()

    fun onEventUpdateValueChange(field: (EventUpdateUiState) -> EventUpdateUiState) {
        _eventUpdateUiState.value = field(_eventUpdateUiState.value)
    }

    fun onEventCreateValueChange(field: (EventCreateUiState) -> EventCreateUiState) {
        _eventCreateUiState.value = field(_eventCreateUiState.value)
    }

    fun createEvent(eventDto: EventDtos.EventCreate) {
        viewModelScope.launch {
            _eventCreateUiState.value = _eventCreateUiState.value.copy(
                isLoading = true,
                errorMessage = null,
                success = false
            )

            when (val result = eventRepository.createEvent(eventDto)) {

                is ApiResult.Success -> {
                    _eventCreateUiState.value =
                        _eventCreateUiState.value.copy(
                            header = "",
                            description = "",
                            eventDate = null,
                            isLoading = false,
                            success = true
                        )

                    _eventUiState.value = _eventUiState.value.copy(
                        events = listOf(result.data!!) + _eventUiState.value.events
                    )
                    _uiEvent.send(UiEvent.Success("Event created successfully"))
                }

                is ApiResult.Error -> {
                    _eventCreateUiState.value = _eventCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = result.message ?: "An unexpected error occurred"
                    )
                    _uiEvent.send(UiEvent.Error("Failed to create event"))
                }

                is ApiResult.Loading -> {
                    _eventCreateUiState.value = _eventCreateUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getEvents(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _eventUiState.value = _eventUiState.value.copy(
                events = if (refresh || page == 0) emptyList() else _eventUiState.value.events,
                isLoading = true,
                errorMessage = null
            )

            when (val result = eventRepository.getEvents(page, size)) {
                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _eventUiState.value = _eventUiState.value.copy(
                        events = if (refresh || page == 0) {
                            response.items
                        } else {
                            _eventUiState.value.events + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _eventUiState.value = _eventUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load events"
                    )
                }

                is ApiResult.Loading -> {
                    _eventUiState.value =
                        _eventUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateEvent(
        eventId: Int,
        eventDto: EventDtos.EventPatch,
    ) {
        viewModelScope.launch {
            _eventUpdateUiState.value =
                _eventUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = eventRepository.updateEvent(eventId, eventDto)) {

                is ApiResult.Success -> {
                    _eventUpdateUiState.value =
                        _eventUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null
                        )

                    val updatedEvents = _eventUiState.value.events.map { event ->
                        if (event.eventId == eventId) event.copy(
                            header = result.data?.header ?: event.header,
                            description = result.data?.description ?: event.description,
                            eventDate = result.data?.eventDate ?: event.eventDate
                        ) else event
                    }

                    _eventUiState.value = _eventUiState.value.copy(events = updatedEvents)
                    _uiEvent.send(UiEvent.Success("Event updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _eventUpdateUiState.value = _eventUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = message
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update event"))
                }

                is ApiResult.Loading -> {
                    _eventUpdateUiState.value =
                        _eventUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}