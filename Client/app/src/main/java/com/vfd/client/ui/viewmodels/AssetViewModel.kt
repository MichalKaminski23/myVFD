package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.data.repositories.AssetRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

data class AssetUpdateUiState(
    val name: String = "",
    val assetType: String = "",
    val description: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val assetRepository: AssetRepository
) : ViewModel() {

    private val _assetUiState = MutableStateFlow(AssetUiState())
    val assetUiState = _assetUiState.asStateFlow()

    private val _assetUpdateUiState = MutableStateFlow(AssetUpdateUiState())
    val assetUpdateUiState = _assetUpdateUiState.asStateFlow()

    fun onAssetUpdateValueChange(field: (AssetUpdateUiState) -> AssetUpdateUiState) {
        _assetUpdateUiState.value = field(_assetUpdateUiState.value)
    }

    private val _assetsFromMyDepartment =
        MutableStateFlow<List<AssetDtos.AssetResponse>>(emptyList())
    val assetsFromMyDepartment = _assetsFromMyDepartment.asStateFlow()

    fun getAssetsFromMyFiredepartment() {
        viewModelScope.launch {
            _assetUiState.value =
                _assetUiState.value.copy(loading = true, error = null, success = false)

            when (val result = assetRepository.getAssetsFromMyFiredepartment()) {

                is ApiResult.Success -> {
                    _assetUiState.value =
                        _assetUiState.value.copy(loading = false, success = true, error = null)
                    _assetsFromMyDepartment.value = result.data ?: emptyList()
                }

                is ApiResult.Error -> {
                    _assetUiState.value = _assetUiState.value.copy(
                        loading = false,
                        success = false,
                        error = result.message ?: "Failed to load assets"
                    )
                    _assetsFromMyDepartment.value = emptyList()
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }
    
    fun updateAsset(
        assetId: Int,
        name: String,
        assetType: String,
        description: String
    ) {
        viewModelScope.launch {
            _assetUpdateUiState.value =
                _assetUpdateUiState.value.copy(loading = true, error = null, success = false)

            val assetDto = AssetDtos.AssetPatch(
                name = name,
                assetType = assetType.ifBlank { null },
                description = description
            )

            when (val result = assetRepository.updateAsset(assetId, assetDto)) {

                is ApiResult.Success -> {
                    _assetUpdateUiState.value =
                        _assetUpdateUiState.value.copy(
                            loading = false,
                            success = true,
                            error = null
                        )

                    _assetsFromMyDepartment.value = _assetsFromMyDepartment.value.map {
                        if (it.assetId == assetId) it.copy(
                            name = result.data?.name ?: it.name,
                            assetTypeName = result.data?.assetTypeName ?: it.assetTypeName,
                            description = result.data?.description ?: it.description
                        ) else it
                    }
                }

                is ApiResult.Error -> {
                    val message = result.message ?: "Unknown error"

                    _assetUpdateUiState.value = _assetUpdateUiState.value.copy(
                        loading = false,
                        success = false,
                        error = message
                    )
                }

                is ApiResult.Loading -> {
                    Unit
                }
            }
        }
    }
}