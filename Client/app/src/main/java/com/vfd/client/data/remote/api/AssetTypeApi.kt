package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.AssetTypeDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetTypeApi {

    @POST("api/asset-types")
    suspend fun createAssetType(
        @Body assetTypeDto: AssetTypeDtos.AssetTypeCreate
    ): AssetTypeDtos.AssetTypeResponse

    @GET("api/asset-types")
    suspend fun getAllAssetTypes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "assetType,asc"
    ): PageResponse<AssetTypeDtos.AssetTypeResponse>

    @GET("api/asset-types/{assetTypeCode}")
    suspend fun getAssetTypeByCode(
        @Path("assetTypeCode") assetTypeCode: String
    ): AssetTypeDtos.AssetTypeResponse

    @PATCH("api/asset-types/{assetTypeCode}")
    suspend fun updateAssetType(
        @Path("assetTypeCode") assetTypeCode: String,
        @Body assetTypeDto: AssetTypeDtos.AssetTypePatch
    ): AssetTypeDtos.AssetTypeResponse
}