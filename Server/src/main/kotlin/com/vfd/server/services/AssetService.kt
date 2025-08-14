package com.vfd.server.services

import com.vfd.server.dtos.AssetDtos
import org.springframework.data.domain.Page

interface AssetService {

    fun createAsset(assetDto: AssetDtos.AssetCreate): AssetDtos.AssetResponse

    fun getAllAssets(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetId,asc"
    ): Page<AssetDtos.AssetResponse>

    fun getAssetById(assetId: Int): AssetDtos.AssetResponse

    fun updateAsset(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse
}