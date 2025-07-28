package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing an asset owned by a fire department.")
data class AssetDto(
    @Schema(
        description = "Unique identifier of the asset.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val assetId: Int? = null,

    @Schema(description = "ID of the fire department that owns the asset.", example = "1")
    @field:NotNull(message = "FiredepartmentId must not be null.")
    val firedepartmentId: Int,

    @Schema(description = "Name of the asset.", example = "Heavy Duty Fire Truck")
    @field:NotBlank(message = "Name must not be blank.")
    @field:Size(max = 128, message = "Name must be at most 128 characters.")
    val name: String,

    @Schema(description = "Type of the asset (e.g., VEHICLE, TOOL).", example = "VEHICLE")
    @field:NotBlank(message = "Asset type must not be blank.")
    @field:Size(max = 16, message = "Asset type must be at most 16 characters.")
    val assetType: String,

    @Schema(
        description = "Detailed description of the asset.",
        example = "A 4x4 fire truck equipped with water tank and rescue gear."
    )
    @field:NotBlank(message = "Description must not be blank.")
    @field:Size(max = 512, message = "Description must be at most 512 characters.")
    val description: String
)