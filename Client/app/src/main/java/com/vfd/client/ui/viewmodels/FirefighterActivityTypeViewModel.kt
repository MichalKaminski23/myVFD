package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos
import com.vfd.client.data.repositories.FirefighterActivityTypeRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FirefighterActivityTypeUiState(
    val firefighterActivityTypes: List<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FirefighterActivityTypeViewModel @Inject constructor(
    private val firefighterActivityTypeRepository: FirefighterActivityTypeRepository
) : ViewModel() {

    private val _firefighterActivityTypeUiState = MutableStateFlow(FirefighterActivityTypeUiState())
    val firefighterActivityTypeUiState = _firefighterActivityTypeUiState.asStateFlow()

    fun getAllFirefighterActivityTypes(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _firefighterActivityTypeUiState.value =
                _firefighterActivityTypeUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result =
                firefighterActivityTypeRepository.getAllFirefighterActivityTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(
                            firefighterActivityTypes = _firefighterActivityTypeUiState.value.firefighterActivityTypes + response.items,
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load activity types"
                        )
                }

                is ApiResult.Loading -> {
                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}