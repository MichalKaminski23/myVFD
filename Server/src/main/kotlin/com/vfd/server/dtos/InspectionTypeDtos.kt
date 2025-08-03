package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object InspectionTypeDtos {

    @Schema(description = "DTO used for creating a new inspection type")
    data class InspectionTypeCreate(
        @field:NotBlank(message = "Inspection type key must not be blank.")
        @field:Size(max = 16, message = "Inspection type key must be at most 16 characters.")
        @field:Schema(description = "Unique code of the inspection type", example = "Technical")
        val inspectionType: String,

        @field:NotBlank(message = "Inspection type name must not be blank.")
        @field:Size(max = 64, message = "Inspection type name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the inspection type", example = "Technical review")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing inspection type")
    data class InspectionTypePatch(
        @field:Size(max = 64, message = "Inspection type name must be at most 64 characters.")
        @field:Schema(description = "Unique code of the inspection type", example = "Technical")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning inspection type information")
    data class InspectionTypeResponse(
        @field:Schema(description = "Unique code of the inspection type", example = "Technical")
        val inspectionType: String,

        @field:Schema(description = "Human-readable name of the inspection type", example = "Technical review")
        val name: String
    )
}