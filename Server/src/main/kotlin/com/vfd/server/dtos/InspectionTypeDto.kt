package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a type of inspection (e.g., TECHNICAL, LEGAL).")
data class InspectionTypeDto(
    @Schema(description = "Unique identifier for the inspection type.", example = "TECHNICAL")
    @field:NotBlank(message = "Inspection type must not be blank.")
    @field:Size(max = 16, message = "Inspection type must be at most 16 characters.")
    val inspectionType: String,

    @Schema(description = "Human-readable name for the inspection type.", example = "Technical inspection")
    @field:NotBlank(message = "Inspection type name must not be blank.")
    @field:Size(max = 64, message = "Inspection type name must be at most 64 characters.")
    val name: String
)
