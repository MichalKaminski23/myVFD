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
        @field:NotBlank(message = "{password.current.not_blank}")
        @field:Schema(description = "Current password", example = "OldPass123!")
        val currentPassword: String,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "{password.new.not_blank}")
        @field:Size(min = 8, max = 128, message = "{password.new.size}")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "{password.new.pattern}"
        )
        @field:Schema(description = "New password", example = "NewPass123!")
        val newPassword: String
    )
}