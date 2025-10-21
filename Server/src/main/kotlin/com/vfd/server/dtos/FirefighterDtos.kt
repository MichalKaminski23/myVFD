package com.vfd.server.dtos

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

    data class FirefighterCreateByEmailAddress(
        @field:NotNull(message = "User email address must not be null.")
        @field:Schema(
            description = "Email address of the user to assign as a firefighter",
            example = "presidentStr@test.com"
        )
        val userEmailAddress: String,

        @field:NotNull(message = "Firedepartment ID must not be null.")
        @field:Schema(description = "ID of the fire department to assign the firefighter to")
        val firedepartmentId: Int
    )

    @Schema(description = "DTO used for partially updating a firefighter record")
    data class FirefighterPatch(
        @Schema(allowableValues = ["ADMIN", "PRESIDENT", "MEMBER", "USER"])
        @field:Schema(description = "Role assigned to the firefighter", example = "MEMBER")
        val role: String? = null,

        @Schema(allowableValues = ["PENDING", "ACTIVE", "REJECTED"])
        @field:Schema(description = "Status assigned to the firefighter", example = "PENDING")
        val status: String? = null
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
        val role: String,

        @field:Schema(description = "Status assigned to the firefighter", example = "PENDING")
        val status: String,

        @field:Schema(description = "Total time on operations in hours", example = "12.5")
        val hours: Double
    )

    @Schema(description = "DTO used for returning firefighter information without personal details")
    data class FirefighterResponseShort(
        @field:Schema(description = "Unique identifier of the firefighter", example = "7")
        val firefighterId: Int,

        @field:Schema(description = "Name of the firefighter", example = "Arek")
        val firstName: String,

        @field:Schema(description = "Surname of the firefighter", example = "Niemusialski")
        val lastName: String,

        @field:Schema(description = "Email address of the firefighter", example = "Arek@test.com")
        val emailAddress: String
    )
}