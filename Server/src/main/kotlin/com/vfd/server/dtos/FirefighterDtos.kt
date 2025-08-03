package com.vfd.server.dtos

import com.vfd.server.entities.Role
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

        @field:NotNull(message = "Role must not be null.")
        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val role: Role
    )

    @Schema(description = "DTO used for partially updating a firefighter record")
    data class FirefighterPatch(
        @field:Schema(description = "ID of the fire department to assign the firefighter to", example = "7")
        val firedepartmentId: Int? = null,

        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val role: Role? = null
    )

    @Schema(description = "DTO used for returning firefighter information")
    data class FirefighterResponse(
        @field:Schema(description = "Unique identifier of the firefighter", example = "7")
        val firefighterId: Int,

        @field:Schema(description = "User assigned to this firefighter record")
        val user: UserDtos.UserResponse,

        @field:Schema(description = "Fire department the firefighter is part of")
        val firedepartment: FiredepartmentDtos.FiredepartmentResponse,

        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val role: Role
    )
}
