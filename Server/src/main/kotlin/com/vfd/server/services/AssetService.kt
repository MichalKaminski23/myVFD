package com.vfd.server.services

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.shared.PageResponse

interface AssetService {

    fun createAsset(assetDto: AssetDtos.AssetCreate): AssetDtos.AssetResponse

    fun getAllAssets(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetId,asc"
    ): PageResponse<AssetDtos.AssetResponse>

    fun getAssetById(assetId: Int): AssetDtos.AssetResponse

    fun getAssetsFromLoggedUser(emailAddress: String): List<AssetDtos.AssetResponse>

    fun updateAsset(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse
}