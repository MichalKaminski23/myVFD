package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.repositories.FirefighterRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurrentFirefighterUiState(
    var currentFirefighter: FirefighterDtos.FirefighterResponse? = null,
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
                }

                is ApiResult.Error -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to create firefighter"
                        )
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
                _currentFirefighterUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firefighterRepository.getFirefighterByEmailAddress()) {

                is ApiResult.Success -> {
                    _currentFirefighterUiState.value.currentFirefighter = result.data
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
                    getPendingFirefighters()
                }

                is ApiResult.Error -> {
                    _currentFirefighterUiState.value =
                        _currentFirefighterUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to change role or status"
                        )
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

    fun getPendingFirefighters(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _pendingFirefightersUiState.value =
                _pendingFirefightersUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firefighterRepository.getPendingFirefighters(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    val merged =
                        (_pendingFirefightersUiState.value.pendingFirefighters + response.items)
                            .distinctBy { it.firefighterId }
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(
                            pendingFirefighters = merged,
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
                }

                is ApiResult.Loading -> {
                    _pendingFirefightersUiState.value =
                        _pendingFirefightersUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFirefighters(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _activeFirefightersUiState.value =
                _activeFirefightersUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firefighterRepository.getFirefighters(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    val merged =
                        (_activeFirefightersUiState.value.activeFirefighters + response.items)
                            .distinctBy { it.firefighterId }
                    _activeFirefightersUiState.value =
                        _activeFirefightersUiState.value.copy(
                            activeFirefighters = merged,
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
                }

                is ApiResult.Loading -> {
                    _activeFirefightersUiState.value =
                        _activeFirefightersUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}