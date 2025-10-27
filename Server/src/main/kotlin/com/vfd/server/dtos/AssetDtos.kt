package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

object AssetDtos {

    @Schema(description = "DTO used for creating a new asset")
    data class AssetCreate(
        @field:NotBlank(message = "{asset.name.not_blank}")
        @field:Size(max = 128, message = "{asset.name.size}")
        @field:Schema(description = "Name of the asset", example = "Pompa 3000 l/min")
        val name: String,

        @field:NotBlank(message = "{asset.assetType.not_blank}")
        @field:Size(max = 16, message = "{asset.assetType.size}")
        @field:Schema(description = "Key of the asset type", example = "POMPWODNA")
        val assetType: String,

        @field:Size(max = 512, message = "{asset.description.size}")
        @field:Schema(
            description = "Optional description of the asset",
            example = "Pompa zapasowa uzywana podczas powodzi"
        )
        val description: String? = null
    )

    @Schema(description = "DTO used for partially updating an existing asset")
    data class AssetPatch(
        @field:Size(max = 128, message = "{asset.name.size}")
        @field:Schema(description = "Name of the asset", example = "Pompa 3000 l/min")
        val name: String? = null,

        @field:Size(max = 16, message = "{asset.assetType.size}")
        @field:Schema(description = "Key of the asset type", example = "POMPWODNA")
        val assetType: String? = null,

        @field:Size(max = 512, message = "{asset.description.size}")
        @field:Schema(description = "Description of the asset", example = "Pompa zapasowa uzywana podczas powodzi")
        val description: String? = null
    )

    @Schema(description = "DTO used for returning asset information")
    data class AssetResponse(
        @field:Schema(description = "Unique identifier of the asset", example = "7")
        val assetId: Int,

        @field:Schema(description = "Firedepartment that owns this asset", example = "3")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the asset", example = "Pompa 3000 l/min")
        val name: String,

        @field:Schema(description = "Type name of the asset", example = "POMPWODNA")
        val assetTypeName: String,

        @field:Schema(
            description = "Optional description of the asset",
            example = "Pompa zapasowa uzywana podczas powodzi"
        )
        val description: String?
    )

    @Schema(description = "DTO used for creating a new asset for development purposes")
    data class AssetCreateDev(
        @field:NotNull(message = "{asset.firedepartmentId.not_null}")
        @field:Schema(description = "ID of the fire department owning the asset", example = "7")
        val firedepartmentId: Int,

        @field:NotBlank(message = "{asset.name.not_blank}")
        @field:Size(max = 128, message = "{asset.name.size}")
        @field:Schema(description = "Name of the asset", example = "Pompa 3000 l/min")
        val name: String,

        @field:NotBlank(message = "{asset.assetType.not_blank}")
        @field:Size(max = 16, message = "{asset.assetType.size}")
        @field:Schema(description = "Key of the asset type", example = "POMPWODNA")
        val assetType: String,

        @field:Size(max = 512, message = "{asset.description.size}")
        @field:Schema(
            description = "Optional description of the asset",
            example = "Pompa zapasowa uzywana podczas powodzi"
        )
        val description: String? = null
    )
}