package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.data.repositories.FiredepartmentRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiredepartmentUiState(
    val firedepartments: List<FiredepartmentDtos.FiredepartmentResponseShort> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class FiredepartmentDetailUiState(
    val firedepartment: FiredepartmentDtos.FiredepartmentResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FiredepartmentViewModel @Inject constructor(
    private val firedepartmentRepository: FiredepartmentRepository
) : ViewModel() {

    private val _firedepartmentUiState = MutableStateFlow(FiredepartmentUiState())
    val firedepartmentUiState = _firedepartmentUiState.asStateFlow()

    private val _firedepartmentDetailUiState = MutableStateFlow(FiredepartmentDetailUiState())
    val firedepartmentDetailUiState = _firedepartmentDetailUiState.asStateFlow()

    fun getFiredepartmentsShort(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _firedepartmentUiState.value =
                _firedepartmentUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firedepartmentRepository.getFiredepartmentsShort(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firedepartmentUiState.value = _firedepartmentUiState.value.copy(
                        firedepartments = response.items,
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _firedepartmentUiState.value = _firedepartmentUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load firedepartments"
                    )
                }

                is ApiResult.Loading -> {
                    _firedepartmentUiState.value =
                        _firedepartmentUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFiredepartment() {
        viewModelScope.launch {
            _firedepartmentDetailUiState.value =
                _firedepartmentDetailUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firedepartmentRepository.getFiredepartment()) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firedepartmentDetailUiState.value = _firedepartmentDetailUiState.value.copy(
                        firedepartment = response,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _firedepartmentDetailUiState.value = _firedepartmentDetailUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load firedepartment"
                    )
                }

                is ApiResult.Loading -> {
                    _firedepartmentDetailUiState.value =
                        _firedepartmentDetailUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}