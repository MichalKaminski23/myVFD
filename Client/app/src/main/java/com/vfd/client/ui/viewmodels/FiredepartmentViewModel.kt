package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.data.repositories.FiredepartmentRepository
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiredepartmentsShortUiState(
    val firedepartmentsShort: List<FiredepartmentDtos.FiredepartmentResponseShort> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class FiredepartmentsUiState(
    val firedepartments: List<FiredepartmentDtos.FiredepartmentResponse> = emptyList(),
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

data class FiredepartmentCreateUiState(
    val name: String = "",
    val address: AddressDtos.AddressCreate
    = AddressDtos.AddressCreate(
        country = "",
        voivodeship = "",
        city = "",
        postalCode = "",
        street = "",
        houseNumber = "",
        apartNumber = null
    ),
    val nrfs: Boolean = false,
    val isLoading: Boolean = false,
    val addressTouched: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

data class FiredepartmentUpdateUiState(
    val name: String = "",
    val nameTouched: Boolean = false,
    val address: AddressDtos.AddressCreate? = null,
    val addressTouched: Boolean = false,
    val nrfs: Boolean = false,
    val nrfsTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class FiredepartmentViewModel @Inject constructor(
    private val firedepartmentRepository: FiredepartmentRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _firedepartmentsShortUiState = MutableStateFlow(FiredepartmentsShortUiState())
    val firedepartmentsShortUiState = _firedepartmentsShortUiState.asStateFlow()

    private val _firedepartmentsUiState = MutableStateFlow(FiredepartmentsUiState())
    val firedepartmentsUiState = _firedepartmentsUiState.asStateFlow()

    private val _firedepartmentDetailUiState = MutableStateFlow(FiredepartmentDetailUiState())
    val firedepartmentDetailUiState = _firedepartmentDetailUiState.asStateFlow()

    private val _firedepartmentCreateUiState = MutableStateFlow(FiredepartmentCreateUiState())
    val firedepartmentCreateUiState = _firedepartmentCreateUiState.asStateFlow()

    private val _firedepartmentUpdateUiState = MutableStateFlow(FiredepartmentUpdateUiState())
    val firedepartmentUpdateUiState = _firedepartmentUpdateUiState.asStateFlow()

    fun onFiredepartmentUpdateValueChange(field: (FiredepartmentUpdateUiState) -> FiredepartmentUpdateUiState) {
        _firedepartmentUpdateUiState.value = field(_firedepartmentUpdateUiState.value)
    }

    fun onFiredepartmentCreateValueChange(field: (FiredepartmentCreateUiState) -> FiredepartmentCreateUiState) {
        _firedepartmentCreateUiState.value = field(_firedepartmentCreateUiState.value)
    }

    fun createFiredepartment(firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate) {
        viewModelScope.launch {
            _firedepartmentCreateUiState.value =
                _firedepartmentCreateUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = firedepartmentRepository.createFiredepartment(firedepartmentDto)) {

                is ApiResult.Success -> {
                    delay(400)
                    _firedepartmentCreateUiState.value = _firedepartmentCreateUiState.value.copy(
                        isLoading = false,
                        success = true,
                        errorMessage = null,
                        fieldErrors = emptyMap()
                    )
                    _uiEvent.send(UiEvent.Success("Firedepartment created successfully"))
                }

                is ApiResult.Error -> {
                    _firedepartmentCreateUiState.value = _firedepartmentCreateUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to create firedepartment"
                    )
                    _uiEvent.send(UiEvent.Error("Failed to create firedepartment"))
                }

                is ApiResult.Loading -> {
                    _firedepartmentCreateUiState.value =
                        _firedepartmentCreateUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateFiredepartment(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ) {
        viewModelScope.launch {
            _firedepartmentUpdateUiState.value =
                _firedepartmentUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = firedepartmentRepository.updateFiredepartment(
                firedepartmentId,
                firedepartmentDto
            )) {

                is ApiResult.Success -> {
                    _firedepartmentUpdateUiState.value = _firedepartmentUpdateUiState.value.copy(
                        isLoading = false,
                        success = true,
                        errorMessage = null,
                        fieldErrors = emptyMap()
                    )

                    val updatedFiredepartments =
                        _firedepartmentsUiState.value.firedepartments.map { firedepartment ->
                            if (firedepartment.firedepartmentId == firedepartmentId) firedepartment.copy(
                                name = result.data?.name ?: firedepartment.name,
                                address = AddressDtos.AddressResponse(
                                    addressId = result.data?.address?.addressId
                                        ?: firedepartment.address.addressId,
                                    country = result.data?.address?.country
                                        ?: firedepartment.address.country,
                                    voivodeship = result.data?.address?.voivodeship
                                        ?: firedepartment.address.voivodeship,
                                    city = result.data?.address?.city
                                        ?: firedepartment.address.city,
                                    postalCode = result.data?.address?.postalCode
                                        ?: firedepartment.address.postalCode,
                                    street = result.data?.address?.street
                                        ?: firedepartment.address.street,
                                    houseNumber = result.data?.address?.houseNumber
                                        ?: firedepartment.address.houseNumber,
                                    apartNumber = result.data?.address?.apartNumber
                                        ?: firedepartment.address.apartNumber
                                ),
                                nrfs = result.data?.nrfs ?: firedepartment.nrfs,
                            ) else firedepartment
                        }
                    _firedepartmentsUiState.value =
                        _firedepartmentsUiState.value.copy(firedepartments = updatedFiredepartments)
                    _uiEvent.send(UiEvent.Success("Firedepartment updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _firedepartmentUpdateUiState.value = _firedepartmentUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update firedepartment"))
                }

                is ApiResult.Loading -> {
                    _firedepartmentDetailUiState.value =
                        _firedepartmentDetailUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFiredepartmentsShort(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _firedepartmentsShortUiState.value =
                _firedepartmentsShortUiState.value.copy(
                    firedepartmentsShort = if (refresh || page == 0) emptyList() else _firedepartmentsShortUiState.value.firedepartmentsShort,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firedepartmentRepository.getFiredepartmentsShort(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firedepartmentsShortUiState.value = _firedepartmentsShortUiState.value.copy(
                        firedepartmentsShort = if (refresh || page == 0) {
                            response.items
                        } else {
                            _firedepartmentsShortUiState.value.firedepartmentsShort + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _firedepartmentsShortUiState.value = _firedepartmentsShortUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load firedepartments"
                    )
                }

                is ApiResult.Loading -> {
                    _firedepartmentsShortUiState.value =
                        _firedepartmentsShortUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun getFiredepartments(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _firedepartmentsUiState.value =
                _firedepartmentsUiState.value.copy(
                    firedepartments = if (refresh || page == 0) emptyList() else _firedepartmentsUiState.value.firedepartments,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firedepartmentRepository.getFiredepartments(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firedepartmentsUiState.value = _firedepartmentsUiState.value.copy(
                        firedepartments = if (refresh || page == 0) {
                            response.items
                        } else {
                            _firedepartmentsUiState.value.firedepartments + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _firedepartmentsUiState.value = _firedepartmentsUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load firedepartments"
                    )
                }

                is ApiResult.Loading -> {
                    _firedepartmentsUiState.value =
                        _firedepartmentsUiState.value.copy(isLoading = true)
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