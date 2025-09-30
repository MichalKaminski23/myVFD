package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.data.repositories.AssetRepository
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

data class AssetUiState(
    val assets: List<AssetDtos.AssetResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class AssetUpdateUiState(
    val name: String = "",
    val assetType: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

data class AssetCreateUiState(
    val name: String = "",
    val assetType: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvents = _uiEvent.receiveAsFlow()

    private val _assetUiState = MutableStateFlow(AssetUiState())
    val assetUiState = _assetUiState.asStateFlow()

    private val _assetUpdateUiState = MutableStateFlow(AssetUpdateUiState())
    val assetUpdateUiState = _assetUpdateUiState.asStateFlow()

    private val _assetCreateUiState = MutableStateFlow(AssetCreateUiState())
    val assetCreateUiState = _assetCreateUiState.asStateFlow()

    fun onAssetUpdateValueChange(field: (AssetUpdateUiState) -> AssetUpdateUiState) {
        _assetUpdateUiState.value = field(_assetUpdateUiState.value)
    }

    fun onAssetCreateValueChange(field: (AssetCreateUiState) -> AssetCreateUiState) {
        _assetCreateUiState.value = field(_assetCreateUiState.value)
    }

    fun createAsset(
        assetDto: AssetDtos.AssetCreate,
    ) {
        viewModelScope.launch {
            _assetCreateUiState.value =
                _assetCreateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = assetRepository.createAsset(assetDto)) {

                is ApiResult.Success -> {
                    _assetCreateUiState.value = _assetCreateUiState.value.copy(
                        name = "",
                        assetType = "",
                        description = "",
                        isLoading = false,
                        success = true
                    )

                    _assetUiState.value = _assetUiState.value.copy(
                        assets = listOf(result.data!!) + _assetUiState.value.assets
                    )
                    _uiEvent.send(UiEvent.Success("Asset created successfully"))
                    getAssets(refresh = true)
                }

                is ApiResult.Error -> {
                    _assetCreateUiState.value = _assetCreateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = result.message ?: "Failed to create asset"
                    )
                    _uiEvent.send(UiEvent.Error("Failed to create asset"))
                }

                is ApiResult.Loading -> {
                    _assetCreateUiState.value =
                        _assetCreateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }

    fun getAssets(page: Int = 0, size: Int = 20, refresh: Boolean = false) {
        viewModelScope.launch {
            _assetUiState.value =
                _assetUiState.value.copy(
                    assets = if (refresh || page == 0) emptyList() else _assetUiState.value.assets,
                    isLoading = true,
                    errorMessage = null
                )

            when (val result = assetRepository.getAssets(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _assetUiState.value =
                        _assetUiState.value.copy(
                            assets = if (refresh || page == 0) {
                                response.items
                            } else {
                                _assetUiState.value.assets + response.items
                            },
                            page = response.page,
                            totalPages = response.totalPages,
                            isLoading = false,
                            errorMessage = null
                        )
                }

                is ApiResult.Error -> {
                    _assetUiState.value = _assetUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message ?: "Failed to load assets"
                    )
                }

                is ApiResult.Loading -> {
                    _assetUiState.value =
                        _assetUiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun updateAsset(
        assetId: Int,
        assetDto: AssetDtos.AssetPatch,
    ) {
        viewModelScope.launch {
            _assetUpdateUiState.value =
                _assetUpdateUiState.value.copy(
                    isLoading = true,
                    errorMessage = null,
                    success = false
                )

            when (val result = assetRepository.updateAsset(assetId, assetDto)) {

                is ApiResult.Success -> {
                    _assetUpdateUiState.value =
                        _assetUpdateUiState.value.copy(
                            isLoading = false,
                            success = true,
                            errorMessage = null
                        )

                    val updatedAssets = _assetUiState.value.assets.map { asset ->
                        if (asset.assetId == assetId) asset.copy(
                            name = result.data?.name ?: asset.name,
                            assetTypeName = result.data?.assetTypeName ?: asset.assetTypeName,
                            description = result.data?.description ?: asset.description
                        ) else asset
                    }

                    _assetUiState.value = _assetUiState.value.copy(assets = updatedAssets)
                    _uiEvent.send(UiEvent.Success("Asset updated successfully"))
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _assetUpdateUiState.value = _assetUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = message
                    )
                    _uiEvent.send(UiEvent.Error("Failed to update asset"))
                }

                is ApiResult.Loading -> {
                    _assetUpdateUiState.value =
                        _assetUpdateUiState.value.copy(
                            isLoading = true
                        )
                }
            }
        }
    }
}