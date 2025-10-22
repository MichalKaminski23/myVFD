package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos
import com.vfd.client.data.repositories.FirefighterActivityTypeRepository
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

data class FirefighterActivityTypeUiState(
    val firefighterActivityTypes: List<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class FirefighterActivityTypeCreateUiState(
    val firefighterActivityType: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

data class FirefighterActivityTypeUpdateUiState(
    val name: String = "",
    val nameTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class FirefighterActivityTypeViewModel @Inject constructor(
    private val firefighterActivityTypeRepository: FirefighterActivityTypeRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _firefighterActivityTypeUiState = MutableStateFlow(FirefighterActivityTypeUiState())
    val firefighterActivityTypeUiState = _firefighterActivityTypeUiState.asStateFlow()

    private val _firefighterActivityTypeCreateUiState = MutableStateFlow(
        FirefighterActivityTypeCreateUiState()
    )
    val firefighterActivityTypeCreateUiState = _firefighterActivityTypeCreateUiState.asStateFlow()

    private val _firefighterActivityTypeUpdateUiState = MutableStateFlow(
        FirefighterActivityTypeUpdateUiState()
    )
    val firefighterActivityTypeUpdateUiState = _firefighterActivityTypeUpdateUiState.asStateFlow()

    fun onFirefighterActivityTypeUpdateValueChange(field: (FirefighterActivityTypeUpdateUiState) -> FirefighterActivityTypeUpdateUiState) {
        _firefighterActivityTypeUpdateUiState.value =
            field(_firefighterActivityTypeUpdateUiState.value)
    }

    fun onFirefighterActivityTypeCreateValueChange(field: (FirefighterActivityTypeCreateUiState) -> FirefighterActivityTypeCreateUiState) {
        _firefighterActivityTypeCreateUiState.value =
            field(_firefighterActivityTypeCreateUiState.value)
    }

    fun createFirefighterActivityType(firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate) {
        viewModelScope.launch {
            _firefighterActivityTypeCreateUiState.value =
                _firefighterActivityTypeCreateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = firefighterActivityTypeRepository.createFirefighterActivityType(
                firefighterActivityTypeDto
            )) {

                is ApiResult.Success -> {
                    _firefighterActivityTypeCreateUiState.value =
                        _firefighterActivityTypeCreateUiState.value.copy(
                            firefighterActivityType = "",
                            name = "",
                            isLoading = false,
                            success = true,
                            errorMessage = null
                        )

                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(
                            firefighterActivityTypes = listOf(result.data!!) + _firefighterActivityTypeUiState.value.firefighterActivityTypes
                        )
                    _uiEvent.send(UiEvent.Success("Firefighter activity type created successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _firefighterActivityTypeCreateUiState.value =
                        _firefighterActivityTypeCreateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = if (fieldErrors.isEmpty()) message else null,
                            fieldErrors = fieldErrors
                        )
                    _uiEvent.send(UiEvent.Error("Failed to create firefighter activity type type"))
                }

                is ApiResult.Loading -> {
                    _firefighterActivityTypeCreateUiState.value =
                        _firefighterActivityTypeCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun updateFirefighterActivityType(
        firefighterActivityTypeCode: String,
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch,
    ) {
        viewModelScope.launch {
            _firefighterActivityTypeUpdateUiState.value =
                _firefighterActivityTypeUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = firefighterActivityTypeRepository.updateFirefighterActivityType(
                firefighterActivityTypeCode,
                firefighterActivityTypeDto
            )) {

                is ApiResult.Success -> {
                    _firefighterActivityTypeUpdateUiState.value =
                        _firefighterActivityTypeUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedFirefighterActivitiesTypes =
                        _firefighterActivityTypeUiState.value.firefighterActivityTypes.map { firefighterActivityType ->
                            if (firefighterActivityType.firefighterActivityType == firefighterActivityTypeCode) firefighterActivityType.copy(
                                name = firefighterActivityTypeDto.name
                                    ?: firefighterActivityType.name
                            ) else firefighterActivityType
                        }

                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(firefighterActivityTypes = updatedFirefighterActivitiesTypes)
                    _uiEvent.send(UiEvent.Success("Firefighter activity type updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _firefighterActivityTypeUpdateUiState.value =
                        _firefighterActivityTypeUpdateUiState.value.copy(
                            isLoading = false,
                            success = false,
                            errorMessage = if (fieldErrors.isEmpty()) message else null,
                            fieldErrors = fieldErrors
                        )
                    _uiEvent.send(UiEvent.Error("Failed to update firefighter activity type"))
                }

                is ApiResult.Loading -> {
                    _firefighterActivityTypeUpdateUiState.value =
                        _firefighterActivityTypeUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getAllFirefighterActivityTypes(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _firefighterActivityTypeUiState.value =
                _firefighterActivityTypeUiState.value.copy(
                    firefighterActivityTypes = if (refresh || page == 0) emptyList() else _firefighterActivityTypeUiState.value.firefighterActivityTypes,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result =
                firefighterActivityTypeRepository.getAllFirefighterActivityTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _firefighterActivityTypeUiState.value =
                        _firefighterActivityTypeUiState.value.copy(
                            firefighterActivityTypes = if (refresh || page == 0) {
                                response.items
                            } else {
                                _firefighterActivityTypeUiState.value.firefighterActivityTypes + response.items
                            },
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