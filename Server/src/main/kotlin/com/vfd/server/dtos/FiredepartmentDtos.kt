package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

object FiredepartmentDtos {

    @Schema(description = "DTO used for creating a new fire department")
    data class FiredepartmentCreate(
        @field:NotBlank(message = "Name must not be blank.")
        @field:Size(max = 128, message = "Name must be at most 128 characters.")
        @field:Schema(description = "Name of the fire department", example = "OSP Strzyzowice")
        val name: String,

        @field:Valid
        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressCreate,

        @field:Schema(
            description = "Whether the department is part of NRFS (National Rescue and Firefighting System)",
            example = "true"
        )
        val nrfs: Boolean = true
    )

    @Schema(description = "DTO used for partially updating a fire department")
    data class FiredepartmentPatch(
        @field:Size(max = 128, message = "Name must be at most 128 characters.")
        @field:Schema(description = "Name of the fire department", example = "OSP Strzyzowice")
        val name: String? = null,

        @field:Valid
        @field:Schema(description = "Firedepartment's address")
        val address: AddressDtos.AddressCreate? = null,

        @field:Schema(
            description = "Whether the department is part of NRFS (National Rescue and Firefighting System)",
            example = "true"
        )
        val nrfs: Boolean? = null
    )

    @Schema(description = "DTO used for returning fire department information")
    data class FiredepartmentResponse(
        @field:Schema(description = "Unique identifier of the fire department", example = "7")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the fire department", example = "OSP Strzyzowice")
        val name: String,

        @field:Schema(description = "Address of the fire department")
        val address: AddressDtos.AddressResponse,

        @field:Schema(
            description = "Whether the department is part of NRFS (National Rescue and Firefighting System)",
            example = "true"
        )
        val nrfs: Boolean
    )

    @Schema(description = "DTO used for returning fire department information in short version")
    data class FiredepartmentResponseShort(
        @field:Schema(description = "Unique identifier of the fire department", example = "7")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the fire department", example = "OSP Strzyzowice")
        val name: String
    )
}