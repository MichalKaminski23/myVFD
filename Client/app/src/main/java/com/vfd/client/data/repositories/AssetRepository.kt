package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.AssetApi
import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AssetRepository @Inject constructor(
    private val assetApi: AssetApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createAsset(assetDto: AssetDtos.AssetCreate): ApiResult<AssetDtos.AssetResponse> =
        safeApiCall { assetApi.createAsset(assetDto) }

    suspend fun getAllAssets(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetId,asc"
    ): ApiResult<PageResponse<AssetDtos.AssetResponse>> =
        safeApiCall { assetApi.getAllAssets(page, size, sort) }

    suspend fun getAssetById(assetId: Int): ApiResult<AssetDtos.AssetResponse> =
        safeApiCall { assetApi.getAssetById(assetId) }

    suspend fun updateAsset(
        assetId: Int,
        assetDto: AssetDtos.AssetPatch
    ): ApiResult<AssetDtos.AssetResponse> =
        safeApiCall { assetApi.updateAsset(assetId, assetDto) }
}