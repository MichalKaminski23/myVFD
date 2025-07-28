package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing a fire department (VFD unit).")
data class FiredepartmentDto(
    @Schema(
        description = "Unique identifier of the fire department.",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val firedepartmentId: Int? = null,

    @Schema(description = "Name of the fire department.", example = "OSP Dabie")
    @field:NotBlank(message = "Fire department name must not be blank.")
    @field:Size(max = 128, message = "Fire department name must be at most 128 characters.")
    val name: String,

    @Schema(description = "Address data")
    @field:Valid
    val address: AddressDto,

    @Schema(
        description = "Whether the department is part of the National Rescue and Firefighting System (NRFS).",
        example = "true"
    )
    val isNRFS: Boolean = true,

    @Schema(description = "List of firefighters assigned to the department.")
    val firefighters: List<FirefighterDto> = emptyList()
)