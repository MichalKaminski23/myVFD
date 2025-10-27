package com.vfd.client.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.InspectionTypeDtos
import com.vfd.client.data.repositories.InspectionTypeRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InspectionTypeUiState(
    val inspectionTypes: List<InspectionTypeDtos.InspectionTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class InspectionTypeCreateUiState(
    val inspectionType: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

data class InspectionTypeUpdateUiState(
    val name: String = "",
    val nameTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class InspectionTypeViewModel @Inject constructor(
    private val inspectionTypeRepository: InspectionTypeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _inspectionTypeUiState = MutableStateFlow(InspectionTypeUiState())
    val inspectionTypeUiState = _inspectionTypeUiState.asStateFlow()

    private val _inspectionTypeCreateUiState = MutableStateFlow(InspectionTypeCreateUiState())
    val inspectionTypeCreateUiState = _inspectionTypeCreateUiState.asStateFlow()

    private val _inspectionTypeUpdateUiState = MutableStateFlow(InspectionTypeUpdateUiState())
    val inspectionTypeUpdateUiState = _inspectionTypeUpdateUiState.asStateFlow()

    fun onInspectionTypeUpdateValueChange(field: (InspectionTypeUpdateUiState) -> InspectionTypeUpdateUiState) {
        _inspectionTypeUpdateUiState.value = field(_inspectionTypeUpdateUiState.value)
    }

    fun onInspectionTypeCreateValueChange(field: (InspectionTypeCreateUiState) -> InspectionTypeCreateUiState) {
        _inspectionTypeCreateUiState.value = field(_inspectionTypeCreateUiState.value)
    }

    fun createInspectionType(inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate) {
        viewModelScope.launch {
            _inspectionTypeCreateUiState.value =
                _inspectionTypeCreateUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = inspectionTypeRepository.createInspectionType(inspectionTypeDto)) {

                is ApiResult.Success -> {
                    _inspectionTypeCreateUiState.value = _inspectionTypeCreateUiState.value.copy(
                        inspectionType = "",
                        name = "",
                        isLoading = false,
                        success = true,
                        errorMessage = null
                    )

                    _inspectionTypeUiState.value = _inspectionTypeUiState.value.copy(
                        inspectionTypes = listOf(result.data!!) + _inspectionTypeUiState.value.inspectionTypes
                    )
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _inspectionTypeCreateUiState.value = _inspectionTypeCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _inspectionTypeCreateUiState.value =
                        _inspectionTypeCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getAllInspectionTypes(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _inspectionTypeUiState.value =
                _inspectionTypeUiState.value.copy(
                    inspectionTypes = if (refresh || page == 0) emptyList() else _inspectionTypeUiState.value.inspectionTypes,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result =
                inspectionTypeRepository.getAllInspectionTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(
                            inspectionTypes = if (refresh || page == 0) {
                                response.items
                            } else {
                                _inspectionTypeUiState.value.inspectionTypes + response.items
                            },
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
                            errorMessage = result.message ?: context.getString(R.string.error)
                        )
                }

                is ApiResult.Loading -> {
                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch,
    ) {
        viewModelScope.launch {
            _inspectionTypeUpdateUiState.value =
                _inspectionTypeUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = inspectionTypeRepository.updateInspectionType(
                inspectionTypeCode,
                inspectionTypeDto
            )) {

                is ApiResult.Success -> {
                    _inspectionTypeUpdateUiState.value =
                        _inspectionTypeUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedInspectionTypes =
                        _inspectionTypeUiState.value.inspectionTypes.map { inspectionType ->
                            if (inspectionType.inspectionType == inspectionTypeCode) inspectionType.copy(
                                name = inspectionTypeDto.name ?: inspectionType.name
                            ) else inspectionType
                        }

                    _inspectionTypeUiState.value =
                        _inspectionTypeUiState.value.copy(inspectionTypes = updatedInspectionTypes)
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _inspectionTypeUpdateUiState.value = _inspectionTypeUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _inspectionTypeUpdateUiState.value =
                        _inspectionTypeUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}