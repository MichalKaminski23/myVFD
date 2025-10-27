package com.vfd.client.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.data.repositories.OperationRepository
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
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

data class OperationUiState(
    val operations: List<OperationDtos.OperationResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class OperationUpdateUiState(
    val operationType: String = "",
    val address: AddressDtos.AddressCreate? = null,
    val operationDate: LocalDateTime? = null,
    val operationEnd: LocalDateTime? = null,
    val description: String = "",
    val participantsIds: MutableSet<Int> = linkedSetOf(),
    val operationTypeTouched: Boolean = false,
    val operationDateTouched: Boolean = false,
    val operationEndTouched: Boolean = false,
    val descriptionTouched: Boolean = false,
    val addressTouched: Boolean = false,
    val participantsTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

data class OperationCreateUiState(
    val operationType: String = "",
    val address: AddressDtos.AddressCreate? = null,
    val operationDate: LocalDateTime? = null,
    val operationEnd: LocalDateTime? = null,
    val description: String = "",
    val participantsIds: MutableSet<Int> = linkedSetOf(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)

@HiltViewModel
class OperationViewModel @Inject constructor(
    private val operationRepository: OperationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _operationUiState = MutableStateFlow(OperationUiState())
    val operationUiState = _operationUiState.asStateFlow()

    private val _operationUpdateUiState = MutableStateFlow(OperationUpdateUiState())
    val operationUpdateUiState = _operationUpdateUiState.asStateFlow()

    private val _operationCreateUiState = MutableStateFlow(OperationCreateUiState())
    val operationCreateUiState = _operationCreateUiState.asStateFlow()

    fun onOperationUpdateValueChange(field: (OperationUpdateUiState) -> OperationUpdateUiState) {
        _operationUpdateUiState.value = field(_operationUpdateUiState.value)
    }

    fun onOperationCreateValueChange(field: (OperationCreateUiState) -> OperationCreateUiState) {
        _operationCreateUiState.value = field(_operationCreateUiState.value)
    }

    fun createOperation(
        operationDto: OperationDtos.OperationCreate,
    ) {
        viewModelScope.launch {
            _operationCreateUiState.value =
                _operationCreateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false,
                    fieldErrors = emptyMap()
                )

            when (val result = operationRepository.createOperation(operationDto)) {
                is ApiResult.Success -> {
                    _operationCreateUiState.value = _operationCreateUiState.value.copy(
                        operationType = "",
                        address = null,
                        operationDate = null,
                        operationEnd = null,
                        description = "",
                        participantsIds = linkedSetOf(),
                        isLoading = false,
                        success = true
                    )

                    _operationUiState.value = _operationUiState.value.copy(
                        operations = listOf(result.data!!) + _operationUiState.value.operations
                    )
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _operationCreateUiState.value = _operationCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _operationCreateUiState.value =
                        _operationCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getOperations(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _operationUiState.value =
                _operationUiState.value.copy(
                    operations = if (refresh || page == 0) emptyList() else _operationUiState.value.operations,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = operationRepository.getOperations(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _operationUiState.value =
                        _operationUiState.value.copy(
                            operations = if (refresh || page == 0) {
                                response.items
                            } else {
                                _operationUiState.value.operations + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _operationUiState.value = _operationUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: context.getString(R.string.error)
                    )
                }

                is ApiResult.Loading -> {
                    _operationUiState.value =
                        _operationUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateOperation(
        operationId: Int,
        operationDto: OperationDtos.OperationPatch,
    ) {
        viewModelScope.launch {
            _operationUpdateUiState.value =
                _operationUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = operationRepository.updateOperation(operationId, operationDto)) {

                is ApiResult.Success -> {
                    _operationUpdateUiState.value =
                        _operationUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedOperations = _operationUiState.value.operations.map { operation ->
                        if (operation.operationId == operationId) operation.copy(
                            operationTypeName = result.data?.operationTypeName
                                ?: operation.operationTypeName,
                            operationDate = result.data?.operationDate ?: operation.operationDate,
                            operationEnd = result.data?.operationEnd ?: operation.operationEnd,
                            address = AddressDtos.AddressResponse(
                                addressId = result.data?.address?.addressId
                                    ?: operation.address.addressId,
                                country = result.data?.address?.country
                                    ?: operation.address.country,
                                voivodeship = result.data?.address?.voivodeship
                                    ?: operation.address.voivodeship,
                                city = result.data?.address?.city
                                    ?: operation.address.city,
                                postalCode = result.data?.address?.postalCode
                                    ?: operation.address.postalCode,
                                street = result.data?.address?.street
                                    ?: operation.address.street,
                                houseNumber = result.data?.address?.houseNumber
                                    ?: operation.address.houseNumber,
                                apartNumber = result.data?.address?.apartNumber
                                    ?: operation.address.apartNumber
                            ),
                            description = result.data?.description ?: operation.description,
                            participants = result.data?.participants ?: operation.participants
                        ) else operation
                    }

                    _operationUiState.value =
                        _operationUiState.value.copy(operations = updatedOperations)
                    _uiEvent.send(UiEvent.Success(context.getString(R.string.success)))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: context.getString(R.string.error)

                    val fieldErrors = result.fieldErrors

                    _operationUpdateUiState.value = _operationUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error(context.getString(R.string.error)))
                }

                is ApiResult.Loading -> {
                    _operationUpdateUiState.value =
                        _operationUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}