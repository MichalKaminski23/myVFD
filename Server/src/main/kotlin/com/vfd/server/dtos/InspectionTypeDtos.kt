package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object InspectionTypeDtos {

    @Schema(description = "DTO used for creating a new inspection type")
    data class InspectionTypeCreate(
        @field:NotBlank(message = "{inspectionType.key.not_blank}")
        @field:Size(max = 16, message = "{inspectionType.key.size}")
        @field:Schema(description = "Unique code of the inspection type", example = "WYMOPON")
        val inspectionType: String,

        @field:NotBlank(message = "{inspectionType.name.not_blank}")
        @field:Size(max = 64, message = "{inspectionType.name.size}")
        @field:Schema(description = "Human-readable name of the inspection type", example = "Wymiana opon")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing inspection type")
    data class InspectionTypePatch(
        @field:Size(max = 64, message = "{inspectionType.name.size}")
        @field:Schema(description = "Unique code of the inspection type", example = "Wymiana opon")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning inspection type information")
    data class InspectionTypeResponse(
        @field:Schema(description = "Unique code of the inspection type", example = "WYMOPON")
        val inspectionType: String,

        @field:Schema(description = "Human-readable name of the inspection type", example = "Wymiana opon")
        val name: String
    )
}