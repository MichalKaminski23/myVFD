package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.data.repositories.AssetRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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

sealed class UiEvent {
    data class Success(val message: String) : UiEvent()
    data class Error(val message: String) : UiEvent()
} // Snackbar

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow() // Snackbar

    private val _assetUiState = MutableStateFlow(AssetUiState())
    val assetUiState = _assetUiState.asStateFlow()

    private val _assetUpdateUiState = MutableStateFlow(AssetUpdateUiState())
    val assetUpdateUiState = _assetUpdateUiState.asStateFlow()

    fun onAssetUpdateValueChange(field: (AssetUpdateUiState) -> AssetUpdateUiState) {
        _assetUpdateUiState.value = field(_assetUpdateUiState.value)
    }

    fun getAssets(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _assetUiState.value =
                _assetUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = assetRepository.getAssets(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    _assetUiState.value =
                        _assetUiState.value.copy(
                            assets = _assetUiState.value.assets + response.items,
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
                    _events.send(UiEvent.Success("Asset updated successfully")) // Snackbar

                    val updatedAssets = _assetUiState.value.assets.map { asset ->
                        if (asset.assetId == assetId) asset.copy(
                            name = result.data?.name ?: asset.name,
                            assetTypeName = result.data?.assetTypeName ?: asset.assetTypeName,
                            description = result.data?.description ?: asset.description
                        ) else asset
                    }

                    _assetUiState.value = _assetUiState.value.copy(assets = updatedAssets)
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _assetUpdateUiState.value = _assetUpdateUiState.value.copy(
                        isLoading = false,
                        success = false,
                        errorMessage = message
                    )
                    _events.send(UiEvent.Error("Failed to update asset"))
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