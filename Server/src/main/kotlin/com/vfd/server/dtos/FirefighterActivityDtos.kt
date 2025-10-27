package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object FirefighterActivityDtos {

    @Schema(description = "DTO used for creating a new firefighter activity record")
    data class FirefighterActivityCreate(
        @field:NotBlank(message = "{firefighterActivity.type.not_blank}")
        @field:Size(max = 16, message = "{firefighterActivity.type.size}")
        @field:Schema(description = "Code of the activity type", example = "KPP")
        val firefighterActivityType: String,

        @field:NotNull(message = "{firefighterActivity.activityDate.not_null}")
        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512, message = "{firefighterActivity.description.size}")
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Skonczony kurs pierwszej pomocy"
        )
        val description: String? = null
    )

    @Schema(description = "DTO used for partially updating a firefighter activity")
    data class FirefighterActivityPatch(
        @field:Size(max = 16, message = "{firefighterActivity.type.size}")
        @field:Schema(description = "Code of the activity type", example = "KPP")
        val firefighterActivityType: String? = null,

        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime? = null,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512, message = "{firefighterActivity.description.size}")
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Skonczony kurs pierwszej pomocy"
        )
        val description: String? = null,

        @Schema(allowableValues = ["PENDING", "ACTIVE", "REJECTED"])
        @field:Schema(description = "Status assigned to the activity", example = "PENDING")
        val status: String? = null

    )

    @Schema(description = "DTO used for returning firefighter activity information")
    data class FirefighterActivityResponse(
        @field:Schema(description = "Unique identifier of the firefighter activity", example = "7")
        val firefighterActivityId: Int,

        @field:Schema(description = "Firefighter who performed the activity", example = "3")
        val firefighterId: Int,

        @field:Schema(description = "Type of the activity", example = "KPP")
        val firefighterActivityTypeName: String,

        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2026-08-03T00:00:00")
        val expirationDate: LocalDateTime?,

        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Skonczony kurs pierwszej pomocy."
        )
        val description: String?,

        @field:Schema(description = "Status assigned to the activity", example = "PENDING")
        val status: String
    )

    @Schema(description = "DTO used for creating a new firefighter activity record for development purposes")
    data class FirefighterActivityCreateDev(
        @field:NotNull(message = "{firefighterActivity.firefighterId.not_null}")
        @field:Schema(description = "ID of the firefighter performing the activity", example = "7")
        val firefighterId: Int,

        @field:NotBlank(message = "{firefighterActivity.type.not_blank}")
        @field:Size(max = 16, message = "{firefighterActivity.type.size}")
        @field:Schema(description = "Code of the activity type", example = "KPP")
        val firefighterActivityType: String,

        @field:NotNull(message = "{firefighterActivity.activityDate.not_null}")
        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512, message = "{firefighterActivity.description.size}")
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Skonczony kurs pierwszej pomocy"
        )
        val description: String? = null
    )
}