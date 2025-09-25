package com.vfd.server.dtos

import com.vfd.server.entities.FirefighterRole
import com.vfd.server.entities.FirefighterStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

object FirefighterDtos {

    @Schema(description = "DTO used for creating a new firefighter record")
    data class FirefighterCreate(
        @field:NotNull(message = "User ID must not be null.")
        @field:Schema(description = "ID of the user to assign as a firefighter", example = "7")
        val userId: Int,

        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the fire department to assign the firefighter to", example = "7")
        val firedepartmentId: Int,
    )

    @Schema(description = "DTO used for partially updating a firefighter record")
    data class FirefighterPatch(
        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val firefighterRole: FirefighterRole? = null,

        @field:Schema(description = "Status assigned to the firefighter", example = "PENDING")
        val status: FirefighterStatus? = null
    )

    @Schema(description = "DTO used for returning firefighter information")
    data class FirefighterResponse(
        @field:Schema(description = "Unique identifier of the firefighter", example = "7")
        val firefighterId: Int,

        @field:Schema(description = "Name of the firefighter", example = "Arek")
        val firstName: String,

        @field:Schema(description = "Surname of the firefighter", example = "Niemusialski")
        val lastName: String,

        @field:Schema(description = "Email address of the firefighter", example = "Arek@test.com")
        val emailAddress: String,

        @field:Schema(description = "User assigned to this firefighter record")
        val userId: Int,

        @field:Schema(description = "Fire department the firefighter is part of")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the fire department the firefighter is part of", example = "OSP Dabie")
        val firedepartmentName: String,

        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val firefighterRole: FirefighterRole,

        @field:Schema(description = "Status assigned to the firefighter", example = "PENDING")
        val status: FirefighterStatus
    )
}