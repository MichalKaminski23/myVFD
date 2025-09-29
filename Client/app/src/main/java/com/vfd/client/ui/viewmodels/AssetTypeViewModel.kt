package com.vfd.client.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vfd.client.data.remote.dtos.AssetTypeDtos
import com.vfd.client.data.repositories.AssetTypeRepository
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssetTypeUiState(
    val assetTypes: List<AssetTypeDtos.AssetTypeResponse> = emptyList(),
    val page: Int = 0,
    val totalPages: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AssetTypeViewModel @Inject constructor(
    private val assetTypeRepository: AssetTypeRepository
) : ViewModel() {

    private val _assetTypeUiState = MutableStateFlow(AssetTypeUiState())
    val assetTypeUiState = _assetTypeUiState.asStateFlow()

    fun getAllAssetTypes(page: Int = 0, size: Int = 20) {
        viewModelScope.launch {
            _assetTypeUiState.value =
                _assetTypeUiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = assetTypeRepository.getAllAssetTypes(page, size)) {

                is ApiResult.Success -> {
                    val response = result.data!!
                    delay(400)
                    _assetTypeUiState.value = _assetTypeUiState.value.copy(
                        assetTypes = _assetTypeUiState.value.assetTypes + response.items,
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
}