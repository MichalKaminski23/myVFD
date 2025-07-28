package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a type of firefighter activity (e.g., MEDICAL, TECHNICAL).")
data class FirefighterActivityTypeDto(
    @Schema(description = "Unique identifier for the activity type.", example = "MEDICAL")
    @field:NotBlank(message = "Activity type must not be blank.")
    @field:Size(max = 16, message = "Activity type must be at most 16 characters.")
    val firefighterActivityType: String,

    @Schema(description = "Human-readable name for the activity type.", example = "Medical training")
    @field:NotBlank(message = "Activity type name must not be blank.")
    @field:Size(max = 64, message = "Activity type name must be at most 64 characters.")
    val name: String
)