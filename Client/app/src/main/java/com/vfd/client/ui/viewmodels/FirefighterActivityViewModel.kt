package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.data.repositories.FirefighterActivityRepository
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

data class FirefighterActivityUiState(
    val activities: List<FirefighterActivityDtos.FirefighterActivityResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class FirefighterActivityUpdateUiState(
    val activityType: String = "",
    val activityDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null,
    val description: String = "",
    val status: String = "",
    val activityTypeTouched: Boolean = false,
    val activityDateTouched: Boolean = false,
    val expirationDateTouched: Boolean = false,
    val descriptionTouched: Boolean = false,
    val statusTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

data class FirefighterActivityCreateUiState(
    val firefighterId: Int = -1,
    val activityType: String = "",
    val activityDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null,
    val description: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class FirefighterActivityViewModel @Inject constructor(
    private val firefighterActivityRepository: FirefighterActivityRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _firefighterActivityUiState = MutableStateFlow(FirefighterActivityUiState())
    val firefighterActivityUiState = _firefighterActivityUiState.asStateFlow()

    private val _firefighterActivityUpdateUiState =
        MutableStateFlow(FirefighterActivityUpdateUiState())
    val firefighterActivityUpdateUiState = _firefighterActivityUpdateUiState.asStateFlow()

    private val _firefighterActivityCreateUiState =
        MutableStateFlow(FirefighterActivityCreateUiState())
    val firefighterActivityCreateUiState = _firefighterActivityCreateUiState.asStateFlow()

    fun onActivityUpdateValueChange(field: (FirefighterActivityUpdateUiState) -> FirefighterActivityUpdateUiState) {
        _firefighterActivityUpdateUiState.value = field(_firefighterActivityUpdateUiState.value)
    }

    fun onActivityCreateValueChange(field: (FirefighterActivityCreateUiState) -> FirefighterActivityCreateUiState) {
        _firefighterActivityCreateUiState.value = field(_firefighterActivityCreateUiState.value)
    }

    fun createFirefighterActivity(
        activityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ) {
        viewModelScope.launch {
            _firefighterActivityCreateUiState.value =
                _firefighterActivityCreateUiState.value.copy(
                    isLoading = true,
                    errorMessage = "",
                    success = false,
                    fieldErrors = emptyMap()
                )

            when (val result =
                firefighterActivityRepository.createFirefighterActivity(activityDto)) {

                is ApiResult.Success -> {
                    _firefighterActivityCreateUiState.value =
                        _firefighterActivityCreateUiState.value.copy(
                            activityType = "",
                            activityDate = null,
                            expirationDate = null,
                            description = "",
                            isLoading = false,
                            success = true
                        )

                    _firefighterActivityUiState.value = _firefighterActivityUiState.value.copy(
                        activities = listOf(result.data!!) + _firefighterActivityUiState.value.activities
                    )
                    _uiEvent.send(UiEvent.Success("Activity created successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _firefighterActivityCreateUiState.value =
                        _firefighterActivityCreateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = if (fieldErrors.isEmpty()) message else null,
                            fieldErrors = fieldErrors
                        )
                    val uiMessage = fieldErrors.values.firstOrNull() ?: message
                    _uiEvent.send(UiEvent.Error(uiMessage))
                }

                is ApiResult.Loading -> {
                    _firefighterActivityCreateUiState.value =
                        _firefighterActivityCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getFirefighterActivities(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _firefighterActivityUiState.value =
                _firefighterActivityUiState.value.copy(
                    activities = if (refresh || page == 0) emptyList() else _firefighterActivityUiState.value.activities,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterActivityRepository.getFirefighterActivities(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firefighterActivityUiState.value =
                        _firefighterActivityUiState.value.copy(
                            activities = if (refresh || page == 0) {
                                response.items
                            } else {
                                _firefighterActivityUiState.value.activities + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _firefighterActivityUiState.value = _firefighterActivityUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load activities"
                    )
                }

                is ApiResult.Loading -> {
                    _firefighterActivityUiState.value =
                        _firefighterActivityUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFirefightersActivities(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _firefighterActivityUiState.value =
                _firefighterActivityUiState.value.copy(
                    activities = if (refresh || page == 0) emptyList() else _firefighterActivityUiState.value.activities,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result =
                firefighterActivityRepository.getFirefightersActivities(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firefighterActivityUiState.value =
                        _firefighterActivityUiState.value.copy(
                            activities = if (refresh || page == 0) {
                                response.items
                            } else {
                                _firefighterActivityUiState.value.activities + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _firefighterActivityUiState.value = _firefighterActivityUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load activities"
                    )
                }

                is ApiResult.Loading -> {
                    _firefighterActivityUiState.value =
                        _firefighterActivityUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateFirefighterActivity(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch,
    ) {
        viewModelScope.launch {
            _firefighterActivityUpdateUiState.value =
                _firefighterActivityUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result =
                firefighterActivityRepository.updateFirefighterActivity(
                    firefighterActivityId,
                    firefighterActivityDto
                )) {

                is ApiResult.Success -> {
                    _firefighterActivityUpdateUiState.value =
                        _firefighterActivityUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedActivities =
                        _firefighterActivityUiState.value.activities.map { activity ->
                            if (activity.firefighterActivityId == firefighterActivityId) activity.copy(
                                firefighterActivityTypeName = result.data?.firefighterActivityTypeName
                                    ?: activity.firefighterActivityTypeName,
                                activityDate = result.data?.activityDate ?: activity.activityDate,
                                expirationDate = result.data?.expirationDate
                                    ?: activity.expirationDate,
                                description = result.data?.description ?: activity.description,
                                status = result.data?.status ?: activity.status
                            )
                            else activity
                        }

                    _firefighterActivityUiState.value =
                        _firefighterActivityUiState.value.copy(activities = updatedActivities)
                    _uiEvent.send(UiEvent.Success("Activity updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _firefighterActivityUpdateUiState.value =
                        _firefighterActivityUpdateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = if (fieldErrors.isEmpty()) message else null,
                            fieldErrors = fieldErrors
                        )
                    val uiMessage = fieldErrors.values.firstOrNull() ?: message
                    _uiEvent.send(UiEvent.Success(uiMessage))
                }

                is ApiResult.Loading -> {
                    _firefighterActivityUpdateUiState.value =
                        _firefighterActivityUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}

