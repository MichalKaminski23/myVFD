package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object AssetTypeDtos {

    @Schema(description = "DTO used for creating a new asset type")
    data class AssetTypeCreate(
        @field:NotBlank(message = "{assetType.key.not_blank}")
        @field:Size(max = 16, message = "{assetType.key.size}")
        @field:Schema(description = "Unique key representing the asset type", example = "GLM")
        val assetType: String,

        @field:NotBlank(message = "{assetType.name.not_blank}")
        @field:Size(max = 64, message = "{assetType.name.size}")
        @field:Schema(description = "Human-readable name of the asset type", example = "Gasniczy Lekki Motopompa")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing asset type")
    data class AssetTypePatch(
        @field:Size(max = 64, message = "{assetType.name.size}")
        @field:Schema(description = "Human-readable name of the asset type", example = "GLM")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning asset type information")
    data class AssetTypeResponse(
        @field:Schema(description = "Unique key representing the asset type", example = "GLM")
        val assetType: String,

        @field:Schema(description = "Human-readable name of the asset type", example = "Gasniczy Lekki Motopompa")
        val name: String
    )
}