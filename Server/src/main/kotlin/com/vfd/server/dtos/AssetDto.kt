package com.vfd.server.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AssetDto(
    val assetId: Int? = null,

    @field:NotNull(message = "FiredepartmentId must not be null.")
    val firedepartmentId: Int,

    @field:NotBlank(message = "Name must not be blank.")
    @field:Size(max = 128, message = "Name must be at most 128 characters.")
    val name: String,

    @field:NotBlank(message = "AssetType must not be blank.")
    @field:Size(max = 16, message = "AssetType must be at most 16 characters.")
    val assetType: String,

    @field:NotBlank(message = "Description must not be blank.")
    @field:Size(max = 512, message = "Description must be at most 512 characters.")
    val description: String
)