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

data class FirefighterUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val notFound: Boolean = false
)

@HiltViewModel
class FirefighterViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository
) : ViewModel() {

    private val _firefighterUiState = MutableStateFlow(FirefighterUiState())
    val firefighterUiState = _firefighterUiState.asStateFlow()

    private val _firefighter = MutableStateFlow<FirefighterDtos.FirefighterResponse?>(null)
    val firefighter = _firefighter.asStateFlow()

    private val _pendingFirefighters =
        MutableStateFlow<List<FirefighterDtos.FirefighterResponse>>(emptyList())
    val pendingFirefighters = _pendingFirefighters.asStateFlow()

    private val _firefightersFromMyFiredepartment =
        MutableStateFlow<List<FirefighterDtos.FirefighterResponse>>(emptyList())
    val firefightersFromMyFiredepartment = _firefightersFromMyFiredepartment.asStateFlow()

    fun createFirefighter(userId: Int, firedepartmentId: Int) {
        viewModelScope.launch {
            _firefighterUiState.value =
                _firefighterUiState.value.copy(loading = true, error = null, success = false)

            val firefighterDto = FirefighterDtos.FirefighterCreate(
                userId = userId,
                firedepartmentId = firedepartmentId
            )

            when (val result = firefighterRepository.createFirefighter(firefighterDto)) {

                is ApiResult.Success -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = true,
                            error = null
                        )
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = result.message ?: "Failed to create firefighter"
                        )
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }

    fun getCurrentFirefighter() {
        viewModelScope.launch {
            _firefighterUiState.value =
                _firefighterUiState.value.copy(loading = true, error = null, success = false)

            when (val result = firefighterRepository.getCurrentFirefighter()) {

                is ApiResult.Success -> {
                    _firefighter.value = result.data
                    _firefighterUiState.value = FirefighterUiState(
                        loading = false,
                        success = true,
                        error = null,
                        notFound = false
                    )
                }

                is ApiResult.Error -> {
                    if (result.code == 404) {
                        _firefighter.value = null
                        _firefighterUiState.value = _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = null,
                            notFound = true
                        )
                    } else {
                        _firefighter.value = null
                        _firefighterUiState.value = _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = result.message ?: "Failed to load current firefighter"
                        )
                    }
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }

    fun getPendingFirefighters() {
        viewModelScope.launch {
            _firefighterUiState.value =
                _firefighterUiState.value.copy(loading = true, error = null, success = false)

            when (val result = firefighterRepository.getPendingFirefighters()) {

                is ApiResult.Success -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = true,
                            error = null
                        )
                    _pendingFirefighters.value = result.data ?: emptyList()
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = result.message ?: "Failed to get pending firefighters"
                        )
                    _pendingFirefighters.value = emptyList()
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }

    fun changeFirefighterRoleOrStatus(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ) {
        viewModelScope.launch {
            _firefighterUiState.value =
                _firefighterUiState.value.copy(loading = true, error = null, success = false)

            when (val result =
                firefighterRepository.updateFirefighter(firefighterId, firefighterDto)) {

                is ApiResult.Success -> {
                    _firefighterUiState.value = _firefighterUiState.value.copy(
                        loading = false,
                        success = true,
                        error = null,
                        notFound = false
                    )
                    getCurrentFirefighter()
                    getPendingFirefighters()
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = result.message ?: "Failed to change role or status"
                        )
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }

    fun getFirefightersFromMyFiredepartment() {
        viewModelScope.launch {
            _firefighterUiState.value =
                _firefighterUiState.value.copy(loading = true, error = null, success = false)

            when (val result = firefighterRepository.getFirefightersFromMyFiredepartment()) {

                is ApiResult.Success -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = true,
                            error = null
                        )
                    _firefightersFromMyFiredepartment.value = result.data ?: emptyList()
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        _firefighterUiState.value.copy(
                            loading = false,
                            success = false,
                            error = result.message ?: "Failed to load firefighters"
                        )
                    _firefightersFromMyFiredepartment.value = emptyList()
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }
}