package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.AssetTypeApi
import com.vfd.client.data.remote.dtos.AssetTypeDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AssetTypeRepository @Inject constructor(
    private val assetTypeApi: AssetTypeApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createAssetType(assetTypeDto: AssetTypeDtos.AssetTypeCreate): ApiResult<AssetTypeDtos.AssetTypeResponse> =
        safeApiCall { assetTypeApi.createAssetType(assetTypeDto) }

    suspend fun getAllAssetTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetType,asc"
    ): ApiResult<PageResponse<AssetTypeDtos.AssetTypeResponse>> =
        safeApiCall { assetTypeApi.getAllAssetTypes(page, size, sort) }

    suspend fun getAssetTypeByCode(assetTypeCode: String): ApiResult<AssetTypeDtos.AssetTypeResponse> =
        safeApiCall { assetTypeApi.getAssetTypeByCode(assetTypeCode) }

    suspend fun updateAssetType(
        assetTypeCode: String,
        assetTypeDto: AssetTypeDtos.AssetTypePatch
    ): ApiResult<AssetTypeDtos.AssetTypeResponse> =
        safeApiCall { assetTypeApi.updateAssetType(assetTypeCode, assetTypeDto) }
}