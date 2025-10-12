package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.InspectionDtos
import com.vfd.client.data.repositories.InspectionRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

data class InspectionUiState(
    val inspections: List<InspectionDtos.InspectionResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class InspectionUpdateUiState(
    val inspectionType: String = "",
    val inspectionDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null,
    val inspectionTypeTouched: Boolean = false,
    val inspectionDateTouched: Boolean = false,
    val expirationDateTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

data class InspectionCreateUiState(
    val assetId: Int = -1,
    val inspectionType: String = "",
    val inspectionDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class InspectionViewModel @Inject constructor(
    private val inspectionRepository: InspectionRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _inspectionUiState = MutableStateFlow(InspectionUiState())
    val inspectionUiState = _inspectionUiState.asStateFlow()

    private val _inspectionUpdateUiState = MutableStateFlow(InspectionUpdateUiState())
    val inspectionUpdateUiState = _inspectionUpdateUiState.asStateFlow()

    private val _inspectionCreateUiState = MutableStateFlow(InspectionCreateUiState())
    val inspectionCreateUiState = _inspectionCreateUiState.asStateFlow()

    fun onInspectionUpdateValueChange(field: (InspectionUpdateUiState) -> InspectionUpdateUiState) {
        _inspectionUpdateUiState.value = field(_inspectionUpdateUiState.value)
    }

    fun onInspectionCreateValueChange(field: (InspectionCreateUiState) -> InspectionCreateUiState) {
        _inspectionCreateUiState.value = field(_inspectionCreateUiState.value)
    }

    fun createInspection(
        inspectionDto: InspectionDtos.InspectionCreate,
    ) {
        viewModelScope.launch {
            _inspectionCreateUiState.value =
                _inspectionCreateUiState.value.copy(
                    isLoading = true,
                    errorMessage = "",
                    success = false,
                    fieldErrors = emptyMap()
                )

            when (val result = inspectionRepository.createInspection(inspectionDto)) {

                is ApiResult.Success -> {
                    _inspectionCreateUiState.value = _inspectionCreateUiState.value.copy(
                        assetId = -1,
                        inspectionType = "",
                        inspectionDate = null,
                        expirationDate = null,
                        isLoading = false,
                        success = true
                    )

                    _inspectionUiState.value = _inspectionUiState.value.copy(
                        inspections = listOf(result.data!!) + _inspectionUiState.value.inspections
                    )
                    _uiEvent.send(UiEvent.Success("Inspection created successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _inspectionCreateUiState.value = _inspectionCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    val uiMessage = fieldErrors.values.firstOrNull() ?: message
                    _uiEvent.send(UiEvent.Error(uiMessage))
                }

                is ApiResult.Loading -> {
                    _inspectionCreateUiState.value =
                        _inspectionCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getInspections(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _inspectionUiState.value =
                _inspectionUiState.value.copy(
                    inspections = if (refresh || page == 0) emptyList() else _inspectionUiState.value.inspections,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = inspectionRepository.getInspections(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _inspectionUiState.value =
                        _inspectionUiState.value.copy(
                            inspections = if (refresh || page == 0) {
                                response.items
                            } else {
                                _inspectionUiState.value.inspections + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _inspectionUiState.value = _inspectionUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load inspections"
                    )
                }

                is ApiResult.Loading -> {
                    _inspectionUiState.value =
                        _inspectionUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateInspection(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch,
    ) {
        viewModelScope.launch {
            _inspectionUpdateUiState.value =
                _inspectionUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = inspectionRepository.updateInspection(inspectionId, inspectionDto)) {

                is ApiResult.Success -> {
                    _inspectionUpdateUiState.value =
                        _inspectionUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedInspections =
                        _inspectionUiState.value.inspections.map { inspection ->
                            if (inspection.inspectionId == inspectionId) inspection.copy(
                                inspectionTypeName = result.data?.inspectionTypeName
                                    ?: inspection.inspectionTypeName,
                                inspectionDate = result.data?.inspectionDate
                                    ?: inspection.inspectionDate,
                                expirationDate = result.data?.expirationDate
                                    ?: inspection.expirationDate
                            ) else inspection
                        }

                    _inspectionUiState.value =
                        _inspectionUiState.value.copy(inspections = updatedInspections)
                    _uiEvent.send(UiEvent.Success("Inspection updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _inspectionUpdateUiState.value = _inspectionUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    val uiMessage = fieldErrors.values.firstOrNull() ?: message
                    _uiEvent.send(UiEvent.Error(uiMessage))
                }

                is ApiResult.Loading -> {
                    _inspectionUpdateUiState.value =
                        _inspectionUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}