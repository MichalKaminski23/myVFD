package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object AssetDtos {

    @Serializable
    data class AssetCreate(
        val firedepartmentId: Int,

        val name: String,

        val assetType: String,

        val description: String
    )

    @Serializable
    data class AssetPatch(
        val name: String? = null,

        val assetType: String? = null,

        val description: String? = null
    )

    @Serializable
    data class AssetResponse(
        val assetId: Int,

        val firedepartmentId: Int,

        val name: String,

        val assetTypeName: String,

        val description: String
    )
}