package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a firefighter and their assignment to a fire department.")
data class FirefighterDto(
    @Schema(
        description = "User ID of the firefighter (same as user ID).",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val firefighterId: Int? = null,

    @Schema(description = "Fire department ID the firefighter belongs to.", example = "1")
    val firedepartmentId: Int,

    @Schema(
        description = "Role of the firefighter within the department (e.g., President, Volunteer).",
        example = "Volunteer"
    )
    @field:NotBlank(message = "Firefighter role must not be blank.")
    @field:Size(max = 16, message = "Firefighter role must be at most 16 characters.")
    val role: String
)
