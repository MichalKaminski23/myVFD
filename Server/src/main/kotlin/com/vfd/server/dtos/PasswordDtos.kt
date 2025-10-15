package com.vfd.server.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

object PasswordDtos {

    @Schema(description = "DTO used for changing user password")
    data class PasswordChange(

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "Current password must not be blank.")
        @field:Schema(description = "Current password", example = "OldPass123!")
        val currentPassword: String,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "New password must not be blank.")
        @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
        )
        @field:Schema(description = "New password", example = "NewPass123!")
        val newPassword: String
    )
}