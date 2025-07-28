package com.vfd.server.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserLoginDto(
    @Schema(description = "User's email address", example = "jan.kowalski@test.com")
    @field:NotBlank(message = "Email address must not be blank.")
    @field:Size(max = 128, message = "Email address must be at most 128 characters.")
    @field:Email(message = "Email address must be a well-formed email address.")
    val emailAddress: String,

    @Schema(description = "Password (min 8 chars, uppercase, digit & special)", example = "P@ssw0rd123!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @field:NotBlank(message = "Password must not be blank.")
    @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
        message = "Password must contain at least one uppercase letter, one digit, and one special character."
    )
    val password: String
)