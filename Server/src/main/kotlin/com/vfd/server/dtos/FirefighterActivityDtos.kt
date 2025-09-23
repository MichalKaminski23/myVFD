package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object FirefighterActivityDtos {

    @Schema(description = "DTO used for creating a new firefighter activity record")
    data class FirefighterActivityCreate(
        @field:NotBlank(message = "Activity type must not be blank.")
        @field:Size(max = 16, message = "Activity type must be at most 16 characters.")
        @field:Schema(description = "Code of the activity type", example = "Medical")
        val firefighterActivityType: String,

        @field:NotNull(message = "Activity date must not be null.")
        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512)
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Completed trauma response training."
        )
        val description: String? = null
    )

    @Schema(description = "DTO used for partially updating a firefighter activity")
    data class FirefighterActivityPatch(
        @field:Size(max = 16, message = "Activity type must be at most 16 characters.")
        @field:Schema(description = "Code of the activity type", example = "Medical")
        val firefighterActivityType: String? = null,

        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime? = null,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512, message = "Description must be at most 512 characters.")
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Completed trauma response training"
        )
        val description: String? = null
    )

    @Schema(description = "DTO used for returning firefighter activity information")
    data class FirefighterActivityResponse(
        @field:Schema(description = "Unique identifier of the firefighter activity", example = "7")
        val firefighterActivityId: Int,

        @field:Schema(description = "Firefighter who performed the activity")
        val firefighter: FirefighterDtos.FirefighterResponse,

        @field:Schema(description = "Type of the activity")
        val firefighterActivityType: FirefighterActivityTypeDtos.FirefighterActivityTypeResponse,

        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2026-08-03T00:00:00")
        val expirationDate: LocalDateTime?,

        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Completed trauma response training."
        )
        val description: String?
    )

    @Schema(description = "DTO used for creating a new firefighter activity record for development purposes")
    data class FirefighterActivityCreateDev(
        @field:NotNull(message = "Firefighter ID must not be null.")
        @field:Schema(description = "ID of the firefighter performing the activity", example = "7")
        val firefighterId: Int,

        @field:NotBlank(message = "Activity type must not be blank.")
        @field:Size(max = 16, message = "Activity type must be at most 16 characters.")
        @field:Schema(description = "Code of the activity type", example = "Medical")
        val firefighterActivityType: String,

        @field:NotNull(message = "Activity date must not be null.")
        @field:Schema(description = "Date of the activity", example = "2025-08-03T15:00:00")
        val activityDate: LocalDateTime,

        @field:Schema(description = "Expiration date of the activity", example = "2025-08-03T15:00:00")
        val expirationDate: LocalDateTime? = null,

        @field:Size(max = 512)
        @field:Schema(
            description = "Description or notes related to the activity",
            example = "Completed trauma response training."
        )
        val description: String? = null
    )
}