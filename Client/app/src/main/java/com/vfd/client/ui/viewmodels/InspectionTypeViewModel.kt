package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.InspectionTypeDtos
import com.vfd.client.data.repositories.InspectionTypeRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InspectionTypeUiState(
    val inspectionTypes: List<InspectionTypeDtos.InspectionTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class InspectionTypeViewModel @Inject constructor(
    private val inspectionTypeRepository: InspectionTypeRepository
) : ViewModel() {

    private val _inspectionTypeUiState = MutableStateFlow(InspectionTypeUiState())
    val inspectionTypeUiState = _inspectionTypeUiState.asStateFlow()

    fun getAllInspectionTypes(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _inspectionTypeUiState.value =
                _inspectionTypeUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result =
                inspectionTypeRepository.getAllInspectionTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(
                            inspectionTypes = _inspectionTypeUiState.value.inspectionTypes + response.items,
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to load inspection types"
                        )
                }

                is ApiResult.Loading -> {
                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }
}