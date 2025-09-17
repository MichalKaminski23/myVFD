package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.data.repositories.FiredepartmentRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiredepartmentUiState(
    val firedepartments: List<FiredepartmentDtos.FiredepartmentResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class FiredepartmentViewModel @Inject constructor(
    private val firedepartmentRepository: FiredepartmentRepository
) : ViewModel() {

    private val _firedepartmentUiState = MutableStateFlow(FiredepartmentUiState())
    val firedepartmentUiState = _firedepartmentUiState.asStateFlow()

    fun getAllFiredepartments(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _firedepartmentUiState.value =
                _firedepartmentUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firedepartmentRepository.getAllFiredepartments(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    _firedepartmentUiState.value = _firedepartmentUiState.value.copy(
                        firedepartments = _firedepartmentUiState.value.firedepartments + response.items,
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
}