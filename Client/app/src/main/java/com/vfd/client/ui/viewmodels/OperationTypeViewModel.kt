package com.vfd.client.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.OperationTypeDtos
import com.vfd.client.data.repositories.OperationTypeRepository
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

data class OperationTypeUiState(
    val operationTypes: List<OperationTypeDtos.OperationTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class OperationTypeCreateUiState(
    val operationType: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

data class OperationTypeUpdateUiState(
    val name: String = "",
    val nameTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class OperationTypeViewModel @Inject constructor(
    private val operationTypeRepository: OperationTypeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _operationTypeUiState = MutableStateFlow(OperationTypeUiState())
    val operationTypeUiState = _operationTypeUiState.asStateFlow()

    private val _operationTypeCreateUiState = MutableStateFlow(OperationTypeCreateUiState())
    val operationTypeCreateUiState = _operationTypeCreateUiState.asStateFlow()

    private val _operationTypeUpdateUiState = MutableStateFlow(OperationTypeUpdateUiState())
    val operationTypeUpdateUiState = _operationTypeUpdateUiState.asStateFlow()

    fun onOperationTypeUpdateValueChange(field: (OperationTypeUpdateUiState) -> OperationTypeUpdateUiState) {
        _operationTypeUpdateUiState.value = field(_operationTypeUpdateUiState.value)
    }

    fun onOperationTypeCreateValueChange(field: (OperationTypeCreateUiState) -> OperationTypeCreateUiState) {
        _operationTypeCreateUiState.value = field(_operationTypeCreateUiState.value)
    }

    fun createOperationType(operationTypeDto: OperationTypeDtos.OperationTypeCreate) {
        viewModelScope.launch {
            _operationTypeCreateUiState.value =
                _operationTypeCreateUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = operationTypeRepository.createOperationType(operationTypeDto)) {

                is ApiResult.Success -> {
                    _operationTypeCreateUiState.value = _operationTypeCreateUiState.value.copy(
                        operationType = "",
                        name = "",
                        isLoading = false,
                        success = true,
                        errorMessage = null
                    )

                    _operationTypeUiState.value = _operationTypeUiState.value.copy(
                        operationTypes = listOf(result.data!!) + _operationTypeUiState.value.operationTypes
                    )
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _operationTypeCreateUiState.value = _operationTypeCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _operationTypeCreateUiState.value =
                        _operationTypeCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getAllOperationTypes(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _operationTypeUiState.value =
                _operationTypeUiState.value.copy(
                    operationTypes = if (refresh || page == 0) emptyList() else _operationTypeUiState.value.operationTypes,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = operationTypeRepository.getAllOperationTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _operationTypeUiState.value = _operationTypeUiState.value.copy(
                        operationTypes = if (refresh || page == 0) {
                            response.items
                        } else {
                            _operationTypeUiState.value.operationTypes + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _operationTypeUiState.value = _operationTypeUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: context.getString(R.string.error)
                    )
                }

                is ApiResult.Loading -> {
                    _operationTypeUiState.value =
                        _operationTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateOperationType(
        operationTypeCode: String,
        operationTypeDto: OperationTypeDtos.OperationTypePatch,
    ) {
        viewModelScope.launch {
            _operationTypeUpdateUiState.value =
                _operationTypeUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result =
                operationTypeRepository.updateOperationType(operationTypeCode, operationTypeDto)) {

                is ApiResult.Success -> {
                    _operationTypeUpdateUiState.value =
                        _operationTypeUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedOperationTypes =
                        _operationTypeUiState.value.operationTypes.map { operationType ->
                            if (operationType.operationType == operationTypeCode) operationType.copy(
                                name = operationTypeDto.name ?: operationType.name
                            ) else operationType
                        }

                    _operationTypeUiState.value =
                        _operationTypeUiState.value.copy(operationTypes = updatedOperationTypes)
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _operationTypeUpdateUiState.value = _operationTypeUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _operationTypeUpdateUiState.value =
                        _operationTypeUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}