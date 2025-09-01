package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object AssetTypeDtos {

    @Serializable
    data class AssetTypeCreate(
        val assetType: String,

        val name: String
    )

    @Serializable
    data class AssetTypePatch(
        val name: String? = null
    )

    @Serializable
    data class AssetTypeResponse(
        val assetType: String,

        val name: String
    )
}