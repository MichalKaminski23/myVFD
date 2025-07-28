package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a type of asset (e.g., VEHICLE, TOOL).")
data class AssetTypeDto(
    @Schema(description = "Unique type identifier for the asset.", example = "VEHICLE")
    @field:NotBlank(message = "Asset type must not be blank.")
    @field:Size(max = 16, message = "Asset type must be at most 16 characters.")
    val assetType: String,

    @Schema(description = "Human-readable name of the asset type.", example = "Fire Truck")
    @field:NotBlank(message = "Asset type name must not be blank.")
    @field:Size(max = 64, message = "Asset type name must be at most 64 characters.")
    val name: String
)