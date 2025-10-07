package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.OperationTypeDtos
import com.vfd.client.data.repositories.OperationTypeRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OperationTypeUiState(
    val operationTypes: List<OperationTypeDtos.OperationTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OperationTypeViewModel @Inject constructor(
    private val operationTypeRepository: OperationTypeRepository
) : ViewModel() {

    private val _operationTypeUiState = MutableStateFlow(OperationTypeUiState())
    val operationTypeUiState = _operationTypeUiState.asStateFlow()

    fun getAllOperationTypes(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _operationTypeUiState.value =
                _operationTypeUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = operationTypeRepository.getAllOperationTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _operationTypeUiState.value = _operationTypeUiState.value.copy(
                        operationTypes = _operationTypeUiState.value.operationTypes + response.items,
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _operationTypeUiState.value = _operationTypeUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load operation types"
                    )
                }

                is ApiResult.Loading -> {
                    _operationTypeUiState.value =
                        _operationTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}