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
)

@HiltViewModel
class FirefighterViewModel @Inject constructor(
    private val firefighterRepository: FirefighterRepository
) : ViewModel() {

    private val _firefighterUiState = MutableStateFlow(FirefighterUiState())
    val firefighterUiState = _firefighterUiState.asStateFlow()

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
}