package com.vfd.server.services

import com.vfd.server.dtos.AssetTypeDtos
import org.springframework.data.domain.Page

interface AssetTypeService {

    fun createAssetType(assetTypeDto: AssetTypeDtos.AssetTypeCreate): AssetTypeDtos.AssetTypeResponse

    fun getAllAssetTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "assetType,asc"
    ): Page<AssetTypeDtos.AssetTypeResponse>

    fun getAssetTypeByCode(assetTypeCode: String): AssetTypeDtos.AssetTypeResponse

    fun updateAssetType(
        assetTypeCode: String,
        assetTypeDto: AssetTypeDtos.AssetTypePatch
    ): AssetTypeDtos.AssetTypeResponse
}