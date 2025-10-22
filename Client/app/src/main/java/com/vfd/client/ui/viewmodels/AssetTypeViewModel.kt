package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AssetTypeDtos
import com.vfd.client.data.repositories.AssetTypeRepository
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

data class AssetTypeUiState(
    val assetTypes: List<AssetTypeDtos.AssetTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class AssetTypeCreateUiState(
    val assetType: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

data class AssetTypeUpdateUiState(
    val name: String = "",
    val nameTouched: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

@HiltViewModel
class AssetTypeViewModel @Inject constructor(
    private val assetTypeRepository: AssetTypeRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _assetTypeUiState = MutableStateFlow(AssetTypeUiState())
    val assetTypeUiState = _assetTypeUiState.asStateFlow()

    private val _assetTypeCreateUiState = MutableStateFlow(AssetTypeCreateUiState())
    val assetTypeCreateUiState = _assetTypeCreateUiState.asStateFlow()

    private val _assetTypeUpdateUiState = MutableStateFlow(AssetTypeUpdateUiState())
    val assetTypeUpdateUiState = _assetTypeUpdateUiState.asStateFlow()

    fun onAssetTypeUpdateValueChange(field: (AssetTypeUpdateUiState) -> AssetTypeUpdateUiState) {
        _assetTypeUpdateUiState.value = field(_assetTypeUpdateUiState.value)
    }

    fun onAssetTypeCreateValueChange(field: (AssetTypeCreateUiState) -> AssetTypeCreateUiState) {
        _assetTypeCreateUiState.value = field(_assetTypeCreateUiState.value)
    }

    fun createAssetType(assetTypeDto: AssetTypeDtos.AssetTypeCreate) {
        viewModelScope.launch {
            _assetTypeCreateUiState.value =
                _assetTypeCreateUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = assetTypeRepository.createAssetType(assetTypeDto)) {

                is ApiResult.Success -> {
                    _assetTypeCreateUiState.value = _assetTypeCreateUiState.value.copy(
                        assetType = "",
                        name = "",
                        isLoading = false,
                        success = true,
                        errorMessage = null
                    )

                    _assetTypeUiState.value = _assetTypeUiState.value.copy(
                        assetTypes = listOf(result.data!!) + _assetTypeUiState.value.assetTypes
                    )
                    _uiEvent.send(UiEvent.Success("Asset type created successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _assetTypeCreateUiState.value = _assetTypeCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error("Failed to create asset type"))
                }

                is ApiResult.Loading -> {
                    _assetTypeCreateUiState.value =
                        _assetTypeCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getAllAssetTypes(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _assetTypeUiState.value =
                _assetTypeUiState.value.copy(
                    assetTypes = if (refresh || page == 0) emptyList() else _assetTypeUiState.value.assetTypes,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = assetTypeRepository.getAllAssetTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _assetTypeUiState.value = _assetTypeUiState.value.copy(
                        assetTypes = if (refresh || page == 0) {
                            response.items
                        } else {
                            _assetTypeUiState.value.assetTypes + response.items
                        },
                        page = response.page,
                        totalPages = response.totalPages,
                        isLoading = false,
                        errorMessage = null
                    )
                }

                is ApiResult.Error -> {
                    _assetTypeUiState.value = _assetTypeUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load asset types"
                    )
                }

                is ApiResult.Loading -> {
                    _assetTypeUiState.value =
                        _assetTypeUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateAssetType(
        assetTypeCode: String,
        assetTypeDto: AssetTypeDtos.AssetTypePatch,
    ) {
        viewModelScope.launch {
            _assetTypeUpdateUiState.value =
                _assetTypeUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = assetTypeRepository.updateAssetType(assetTypeCode, assetTypeDto)) {

                is ApiResult.Success -> {
                    _assetTypeUpdateUiState.value =
                        _assetTypeUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )

                    val updatedAssetTypes = _assetTypeUiState.value.assetTypes.map { assetType ->
                        if (assetType.assetType == assetTypeCode) assetType.copy(
                            name = assetTypeDto.name ?: assetType.name
                        ) else assetType
                    }

                    _assetTypeUiState.value =
                        _assetTypeUiState.value.copy(assetTypes = updatedAssetTypes)
                    _uiEvent.send(UiEvent.Success("Asset type updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    val fieldErrors = result.fieldErrors

                    _assetTypeUpdateUiState.value = _assetTypeUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = if (fieldErrors.isEmpty()) message else null,
                        fieldErrors = fieldErrors
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update asset type"))
                }

                is ApiResult.Loading -> {
                    _assetTypeUpdateUiState.value =
                        _assetTypeUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}