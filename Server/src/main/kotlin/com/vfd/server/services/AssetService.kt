package com.vfd.server.services

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.shared.PageResponse

interface AssetService {

    fun createAsset(
        emailAddress: String,
        assetDto: AssetDtos.AssetCreate
    ): AssetDtos.AssetResponse

    fun getAssets(
        page: Int = 0,
        size: Int = 20,
        sort: String = "name,asc",
        emailAddress: String
    ): PageResponse<AssetDtos.AssetResponse>

    fun updateAsset(
        emailAddress: String,
        assetId: Int,
        assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse

    fun createAssetDev(assetDto: AssetDtos.AssetCreateDev): AssetDtos.AssetResponse

    fun getAllAssetsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetId,asc"
    ): PageResponse<AssetDtos.AssetResponse>

    fun getAssetByIdDev(assetId: Int): AssetDtos.AssetResponse

    fun updateAssetDev(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse


}