package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

object AssetDtos {

    @Schema(description = "DTO used for creating a new asset")
    data class AssetCreate(
        @field:NotBlank(message = "Name must not be blank.")
        @field:Size(max = 128, message = "Name must be at most 128 characters.")
        @field:Schema(description = "Name of the asset", example = "Pump 3000 l/min")
        val name: String,

        @field:NotBlank(message = "Asset type must not be blank.")
        @field:Size(max = 16, message = "Asset type must be at most 16 characters.")
        @field:Schema(description = "Key of the asset type", example = "WaterPump")
        val assetType: String,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(description = "Optional description of the asset", example = "Backup pump used during floods")
        val description: String? = null
    )

    @Schema(description = "DTO used for partially updating an existing asset")
    data class AssetPatch(
        @field:Size(max = 128, message = "Name must be at most 128 characters.")
        @field:Schema(description = "Name of the asset", example = "Pump 3000 l/min")
        val name: String? = null,

        @field:Size(max = 16, message = "Asset type must be at most 16 characters.")
        @field:Schema(description = "Key of the asset type", example = "WaterPump")
        val assetType: String? = null,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(description = "Description of the asset", example = "Backup pump used during floods")
        val description: String? = null
    )

    @Schema(description = "DTO used for returning asset information")
    data class AssetResponse(
        @field:Schema(description = "Unique identifier of the asset", example = "7")
        val assetId: Int,

        @field:Schema(description = "Firedepartment that owns this asset")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the asset", example = "Pump 3000 l/min")
        val name: String,

        @field:Schema(description = "Type name of the asset")
        val assetTypeName: String,

        @field:Schema(
            description = "Optional description of the asset",
            example = "Backup pump used during floods"
        )
        val description: String?
    )

    @Schema(description = "DTO used for creating a new asset for development purposes")
    data class AssetCreateDev(
        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the fire department owning the asset", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "Name must not be blank.")
        @field:Size(max = 128, message = "Name must be at most 128 characters.")
        @field:Schema(description = "Name of the asset", example = "Pump 3000 l/min")
        val name: String,

        @field:NotBlank(message = "Asset type must not be blank.")
        @field:Size(max = 16, message = "Asset type must be at most 16 characters.")
        @field:Schema(description = "Key of the asset type", example = "WaterPump")
        val assetType: String,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(description = "Optional description of the asset", example = "Backup pump used during floods")
        val description: String? = null
    )
}