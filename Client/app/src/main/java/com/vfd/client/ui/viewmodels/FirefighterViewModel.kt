package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.HoursResponseDto
import com.vfd.client.data.repositories.FirefighterRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrentFirefighterUiState(
    var currentFirefighter: FirefighterDtos.FirefighterResponse? = null,
    var hours: HoursResponseDto? = null,
    val isLoading: Boolean = false,
    val notFound: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

data class ActiveFirefightersUiState(
    val activeFirefighters: List<FirefighterDtos.FirefighterResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class PendingFirefightersUiState(
    val pendingFirefighters: List<FirefighterDtos.FirefighterResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FirefighterViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _currentFirefighterUiState = MutableStateFlow(CurrentFirefighterUiState())
    val currentFirefighterUiState = _currentFirefighterUiState.asStateFlow()

    private val _activeFirefightersUiState = MutableStateFlow(ActiveFirefightersUiState())
    val activeFirefightersUiState = _activeFirefightersUiState.asStateFlow()

    private val _pendingFirefightersUiState = MutableStateFlow(PendingFirefightersUiState())
    val pendingFirefightersUiState = _pendingFirefightersUiState.asStateFlow()

    fun createFirefighter(firefighterDto: FirefighterDtos.FirefighterCreate) {
        viewModelScope.launch {
            _currentFirefighterUiState.value =
                _currentFirefighterUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firefighterRepository.createFirefighter(firefighterDto)) {

                is ApiResult.Success -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            currentFirefighter = result.data,
                            isLoading = false,
                            errorMessage = null,
                            success = true
                        )
                    _uiEvent.send(UiEvent.Success("Firefighter created successfully"))
                }

                is ApiResult.Error -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to create firefighter"
                        )
                    _uiEvent.send(UiEvent.Success(result.message!!))
                }

                is ApiResult.Loading -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getFirefighterByEmailAddress() {
        viewModelScope.launch {
            _currentFirefighterUiState.value =
                _currentFirefighterUiState.value.copy(
                    currentFirefighter = null,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterRepository.getFirefighterByEmailAddress()) {

                is ApiResult.Success -> {
                    _currentFirefighterUiState.value.currentFirefighter = result.data
                    delay(400)
                    _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                        currentFirefighter = result.data,
                        isLoading = false,
                        errorMessage = null,
                        notFound = false,
                        success = true
                    )
                }

                is ApiResult.Error -> {
                    if (result.code == 404) {
                        _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                            currentFirefighter = null,
                            isLoading = false,
                            errorMessage = null,
                            notFound = true,
                            success = true
                        )
                    } else {
                        _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                            currentFirefighter = null,
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load current firefighter"
                        )
                    }
                }

                is ApiResult.Loading -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun changeFirefighterRoleOrStatus(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ) {
        viewModelScope.launch {
            _currentFirefighterUiState.value =
                _currentFirefighterUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result =
                firefighterRepository.updateFirefighter(firefighterId, firefighterDto)) {

                is ApiResult.Success -> {
                    _pendingFirefightersUiState.value = _pendingFirefightersUiState.value.copy(
                        pendingFirefighters = _pendingFirefightersUiState.value.pendingFirefighters
                            .filterNot { it.firefighterId == firefighterId },
                        isLoading = false
                    )
                    _activeFirefightersUiState.value = _activeFirefightersUiState.value.copy(
                        activeFirefighters = _activeFirefightersUiState.value.activeFirefighters
                            .filterNot { it.firefighterId == firefighterId },
                        isLoading = false
                    )
                    _uiEvent.send(UiEvent.Success("Firefighter updated successfully"))
                }

                is ApiResult.Error -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to change role or status"
                        )
                    _uiEvent.send(UiEvent.Success(result.message!!))
                }

                is ApiResult.Loading -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getPendingFirefighters(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _pendingFirefightersUiState.value =
                _pendingFirefightersUiState.value.copy(
                    pendingFirefighters = if (refresh || page == 0) emptyList() else _pendingFirefightersUiState.value.pendingFirefighters,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterRepository.getPendingFirefighters(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(
                            pendingFirefighters = if (refresh || page == 0) {
                                response.items
                            } else {
                                _pendingFirefightersUiState.value.pendingFirefighters + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load pending firefighters"
                        )
                    _uiEvent.send(UiEvent.Success(result.message!!))
                }

                is ApiResult.Loading -> {
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFirefighters(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _activeFirefightersUiState.value =
                _activeFirefightersUiState.value.copy(
                    activeFirefighters = if (refresh || page == 0) emptyList() else _activeFirefightersUiState.value.activeFirefighters,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterRepository.getFirefighters(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _activeFirefightersUiState.value =
                        _activeFirefightersUiState.value.copy(
                            activeFirefighters = if (refresh || page == 0) {
                                response.items
                            } else {
                                _activeFirefightersUiState.value.activeFirefighters + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _activeFirefightersUiState.value =
                        _activeFirefightersUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load active firefighters"
                        )
                    _uiEvent.send(UiEvent.Success(result.message!!))
                }

                is ApiResult.Loading -> {
                    _activeFirefightersUiState.value =
                        _activeFirefightersUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun deleteFirefighter(firefighterId: Int) {
        viewModelScope.launch {
            when (val result = firefighterRepository.deleteFirefighter(firefighterId)) {
                is ApiResult.Success -> {
                    _pendingFirefightersUiState.value = _pendingFirefightersUiState.value.copy(
                        pendingFirefighters = _pendingFirefightersUiState.value.pendingFirefighters
                            .filterNot { it.firefighterId == firefighterId },
                        isLoading = false
                    )
                    _uiEvent.send(UiEvent.Success("Firefighter deleted successfully"))
                }

                is ApiResult.Error -> {
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to delete firefighter"
                        )
                    _uiEvent.send(UiEvent.Success(result.message!!))
                }

                is ApiResult.Loading -> {
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getHoursForQuarter(year: Int, quarter: Int) {
        viewModelScope.launch {
            _currentFirefighterUiState.value =
                _currentFirefighterUiState.value.copy(
                    hours = null,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterRepository.getHoursForQuarter(year, quarter)) {

                is ApiResult.Success -> {
                    _currentFirefighterUiState.value.hours = result.data
                    delay(400)
                    _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                        hours = result.data,
                        isLoading = false,
                        errorMessage = null,
                        notFound = false,
                        success = true
                    )
                }

                is ApiResult.Error -> {
                    if (result.code == 404) {
                        _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                            hours = null,
                            isLoading = false,
                            errorMessage = null,
                            notFound = true,
                            success = true
                        )
                    } else {
                        _currentFirefighterUiState.value = _currentFirefighterUiState.value.copy(
                            hours = null,
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load hours"
                        )
                        _uiEvent.send(UiEvent.Success(result.message!!))
                    }
                }

                is ApiResult.Loading -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}