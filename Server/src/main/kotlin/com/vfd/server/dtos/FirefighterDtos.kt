package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

object FirefighterDtos {

    @Schema(description = "DTO used for creating a new firefighter record")
    data class FirefighterCreate(
        @field:NotNull(message = "{firefighter.userId.not_null}")
        @field:Schema(description = "ID of the user to assign as a firefighter", example = "7")
        var userId: Int,

        @field:NotNull(message = "{firefighter.firedepartmentId.not_null}")
        @field:Schema(description = "ID of the fire department to assign the firefighter to", example = "7")
        var firedepartmentId: Int,
    )

    data class FirefighterCreateByEmailAddress(
        @field:NotNull(message = "{firefighter.userEmailAddress.not_null}")
        @field:Schema(
            description = "Email address of the user to assign as a firefighter",
            example = "superZiutek@test.com"
        )
        var userEmailAddress: String,

        @field:NotNull(message = "{firefighter.firedepartmentId.not_null}")
        @field:Schema(description = "ID of the fire department to assign the firefighter to", example = "7")
        var firedepartmentId: Int
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

        @field:Schema(description = "Name of the firefighter", example = "Super")
        val firstName: String,

        @field:Schema(description = "Surname of the firefighter", example = "Ziutek")
        val lastName: String,

        @field:Schema(description = "Email address of the firefighter", example = "superZiutek@test.com")
        val emailAddress: String,

        @field:Schema(description = "User assigned to this firefighter record", example = "15")
        val userId: Int,

        @field:Schema(description = "Fire department the firefighter is part of", example = "3")
        val firedepartmentId: Int,

        @field:Schema(description = "Name of the fire department the firefighter is part of", example = "OSP Ziutkowo")
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

        @field:Schema(description = "Name of the firefighter", example = "Super")
        val firstName: String,

        @field:Schema(description = "Surname of the firefighter", example = "Ziutek")
        val lastName: String,

        @field:Schema(description = "Email address of the firefighter", example = "superZiutek@test.com")
        val emailAddress: String
    )
}