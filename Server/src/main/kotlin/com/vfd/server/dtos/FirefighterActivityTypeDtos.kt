package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object FirefighterActivityTypeDtos {

    @Schema(description = "DTO used for creating a new firefighter activity type")
    data class FirefighterActivityTypeCreate(
        @field:NotBlank(message = "Activity type key must not be blank.")
        @field:Size(max = 16, message = "Activity type key must be at most 16 characters.")
        @field:Schema(description = "Unique code for the activity type", example = "Medical")
        val firefighterActivityType: String,

        @field:NotBlank(message = "Activity type name must not be blank.")
        @field:Size(max = 64, message = "Activity type name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the activity type", example = "Medical training")
        val name: String
    )

    @Schema(description = "DTO used for partially updating an existing firefighter activity type")
    data class FirefighterActivityTypePatch(
        @field:Size(max = 64, message = "Activity type name must be at most 64 characters.")
        @field:Schema(description = "Human-readable name of the activity type", example = "Medical training")
        val name: String? = null
    )

    @Schema(description = "DTO used for returning firefighter activity type information")
    data class FirefighterActivityTypeResponse(
        @field:Schema(description = "Unique code for the activity type", example = "Medical")
        val firefighterActivityType: String,

        @field:Schema(description = "Human-readable name of the activity type", example = "Medical training")
        val name: String
    )
}
