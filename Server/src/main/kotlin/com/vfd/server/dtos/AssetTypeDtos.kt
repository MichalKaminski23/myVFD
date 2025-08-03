package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object AssetTypeDtos {

    @Schema(description = "DTO used for creating a new asset type")
    data class AssetTypeCreate(
        @field:NotBlank(message = "Asset type key must not be blank.")
        @field:Size(max = 16, message = "Asset type key must be at most 16 characters.")
        @field:Schema(description = "Unique key representing the asset type", example = "WaterPump")
        val assetType: String,

        @field:NotBlank(message = "Asset type name must not be blank.")
        @field:Size(max = 64, message = "Asset type name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the asset type", example = "Heavy water pump")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing asset type")
    data class AssetTypePatch(
        @field:Size(max = 64, message = "Asset type name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the asset type", example = "Heavy water pump")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning asset type information")
    data class AssetTypeResponse(
        @field:Schema(description = "Unique key representing the asset type", example = "WaterPump")
        val assetType: String,

        @field:Schema(description = "Human-readable name of the asset type", example = "Heavy water pump")
        val name: String
    )
}