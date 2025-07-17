package com.vfd.server.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserLoginDto(
    @field:NotBlank(message = "EmailAddress must not be blank.")
    @field:Size(max = 128, message = "EmailAddress must be at most 128 characters.")
    @field:Email(message = "EmailAddress must be a well-formed email address.")
    val emailAddress: String,

    @field:NotBlank(message = "Password must not be blank.")
    @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    @field:Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
        message = "Password must contain at least one uppercase letter, one digit, and one special character."
    )
    val password: String
)