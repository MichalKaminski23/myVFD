package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.AssetDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetApi {

    @POST("api/assets")
    suspend fun createAsset(@Body assetDto: AssetDtos.AssetCreate): AssetDtos.AssetResponse

    @GET("api/assets")
    suspend fun getAllAssets(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "assetId,asc"
    ): PageResponse<AssetDtos.AssetResponse>

    @GET("api/assets/{assetId}")
    suspend fun getAssetById(@Path("assetId") assetId: Int): AssetDtos.AssetResponse

    @GET("api/assets/my")
    suspend fun getAssetsFromMyFiredepartment(): List<AssetDtos.AssetResponse>

    @PATCH("api/assets/{assetId}")
    suspend fun updateAsset(
        @Path("assetId") assetId: Int,
        @Body assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse
}