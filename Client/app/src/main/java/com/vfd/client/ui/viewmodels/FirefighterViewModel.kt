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

    fun crateFirefighter(userId: Int, firedepartmentId: Int) {
        val createState = _firefighterUiState.value
        viewModelScope.launch {
            _firefighterUiState.value =
                createState.copy(loading = true, error = null, success = false)

            val firefighterDto = FirefighterDtos.FirefighterCreate(
                userId = userId,
                firedepartmentId = firedepartmentId
            )

            when (val result = firefighterRepository.createFirefighter(firefighterDto)) {
                is ApiResult.Success -> {
                    _firefighterUiState.value = createState.copy(loading = false, success = true)
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        createState.copy(loading = false, error = result.message ?: "Unknown error")
                }

                is ApiResult.Loading -> {
                    _firefighterUiState.value = createState.copy(loading = true)
                }
            }
        }
    }

    fun getCurrentFirefighter() {
        viewModelScope.launch {
            _firefighterUiState.value = _firefighterUiState.value.copy(loading = true, error = null)

            when (val result = firefighterRepository.getCurrentFirefighter()) {

                is ApiResult.Success -> {
                    _firefighter.value = result.data
                    _firefighterUiState.value = FirefighterUiState(
                        loading = false,
                        success = true,
                        notFound = false
                    )
                }

                is ApiResult.Error -> {
                    if (result.code == 404) {
                        _firefighter.value = null
                        _firefighterUiState.value = _firefighterUiState.value.copy(
                            loading = false,
                            error = null,
                            success = false,
                            notFound = true
                        )
                    } else {
                        _firefighter.value = null
                        _firefighterUiState.value = _firefighterUiState.value.copy(
                            loading = false,
                            error = result.message ?: "Failed to load current firefighter"
                        )
                    }
                }

                is ApiResult.Loading -> {
                    _firefighterUiState.value = _firefighterUiState.value.copy(loading = true)
                }
            }
        }
    }

    fun getPendingFirefighters() {
        viewModelScope.launch {
            when (val result = firefighterRepository.getPendingFirefighters()) {
                is ApiResult.Success -> _pendingFirefighters.value = result.data ?: emptyList()
                is ApiResult.Error -> _pendingFirefighters.value = emptyList()
                is ApiResult.Loading -> {
                }
            }
        }
    }

    fun changeFirefighterRoleOrStatus(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ) {
        val updateState = _firefighterUiState.value
        viewModelScope.launch {
            _firefighterUiState.value =
                updateState.copy(loading = true, error = null, success = false)

            when (val result =
                firefighterRepository.updateFirefighter(firefighterId, firefighterDto)) {
                is ApiResult.Success -> {
                    _firefighterUiState.value = updateState.copy(loading = false, success = true)
                    getCurrentFirefighter()
                    getPendingFirefighters()
                }

                is ApiResult.Error -> {
                    _firefighterUiState.value =
                        updateState.copy(loading = false, error = result.message ?: "Unknown error")
                }

                is ApiResult.Loading -> {
                    _firefighterUiState.value = updateState.copy(loading = true)
                }
            }
        }
    }
}