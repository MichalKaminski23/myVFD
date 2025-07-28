package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "DTO representing an inspection record for a fire department asset.")
data class InspectionDto(
    @Schema(
        description = "Unique identifier of the inspection.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val inspectionId: Int? = null,

    @Schema(description = "ID of the asset being inspected.", example = "1")
    val assetId: Int,

    @Schema(description = "Type of inspection (e.g., TECHNICAL, LEGAL).", example = "TECHNICAL")
    @field:NotNull(message = "Inspection type must not be null.")
    val inspectionType: String,

    @Schema(description = "Date when the inspection was carried out.", example = "2025-07-28T10:30:00")
    @field:NotNull(message = "Inspection date must not be null.")
    val inspectionDate: LocalDateTime,

    @Schema(description = "Date when the inspection validity expires (if applicable).", example = "2026-07-28T10:30:00")
    val expirationDate: LocalDateTime? = null
)
