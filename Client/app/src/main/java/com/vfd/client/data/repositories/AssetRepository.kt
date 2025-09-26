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

    suspend fun getAssets(
        page: Int = 0,
        size: Int = 20,
        sort: String = "name,asc"
    ): ApiResult<PageResponse<AssetDtos.AssetResponse>> =
        safeApiCall { assetApi.getAssets(page, size, sort) }

    suspend fun updateAsset(
        assetId: Int,
        assetDto: AssetDtos.AssetPatch
    ): ApiResult<AssetDtos.AssetResponse> =
        safeApiCall { assetApi.updateAsset(assetId, assetDto) }
}