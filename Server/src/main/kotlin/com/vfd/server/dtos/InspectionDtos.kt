package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object InspectionDtos {

    @Schema(description = "DTO used for creating a new inspection record")
    data class InspectionCreate(
        @field:NotNull(message = "Asset ID must not be null.")
        @field:Schema(description = "ID of the asset being inspected", example = "7")
        val assetId: Int,

        @field:NotBlank(message = "Inspection type must not be blank.")
        @field:Size(max = 16, message = "Inspection type must be at most 16 characters.")
        @field:Schema(description = "Code of the inspection type", example = "Technical")
        val inspectionType: String,

        @field:NotNull(message = "Inspection date must not be null.")
        @field:Schema(description = "Date when the inspection was performed", example = "2025-08-03T10:00:00")
        val inspectionDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the inspection validity", example = "2026-08-03T00:00:00")
        val expirationDate: LocalDateTime? = null
    )

    @Schema(description = "DTO used for partially updating an inspection")
    data class InspectionPatch(
        @field:Schema(description = "ID of the asset being inspected", example = "7")
        val assetId: Int? = null,

        @field:NotBlank(message = "Inspection type must not be blank.")
        @field:Size(max = 16, message = "Inspection type must be at most 16 characters.")
        @field:Schema(description = "Code of the inspection type", example = "Technical")
        val inspectionType: String? = null,

        @field:Schema(description = "Date when the inspection was performed", example = "2025-09-10T09:00:00")
        val inspectionDate: LocalDateTime? = null,

        @field:Schema(description = "Expiration date of the inspection validity", example = "2026-09-10T00:00:00")
        val expirationDate: LocalDateTime? = null
    )

    @Schema(description = "DTO used for returning inspection information")
    data class InspectionResponse(
        @field:Schema(description = "Unique identifier of the inspection", example = "7")
        val inspectionId: Int,

        @field:Schema(description = "Asset that was inspected")
        val asset: AssetDtos.AssetResponse,

        @field:Schema(description = "Type of the inspection")
        val inspectionType: InspectionTypeDtos.InspectionTypeResponse,

        @field:Schema(description = "Date when the inspection was performed", example = "2025-08-03T10:00:00")
        val inspectionDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the inspection validity", example = "2026-08-03T00:00:00")
        val expirationDate: LocalDateTime?
    )
}