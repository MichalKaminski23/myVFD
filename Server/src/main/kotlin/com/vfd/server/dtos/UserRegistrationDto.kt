package com.vfd.server.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRegistrationDto(
    @Schema(description = "User's first name", example = "Jan")
    @field:NotBlank(message = "First name must not be blank.")
    @field:Size(max = 128, message = "First name must be at most 128 characters.")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters.")
    val firstName: String,

    @Schema(description = "User's last name", example = "Kowalski")
    @field:NotBlank(message = "Last name must not be blank.")
    @field:Size(max = 128, message = "Last name must be at most 128 characters.")
    @field:Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters.")
    val lastName: String,

    @Schema(description = "Address data")
    @field:Valid
    val address: AddressDto,

    @Schema(description = "User's email address", example = "jan.kowalski@test.com")
    @field:NotBlank(message = "Email address must not be blank.")
    @field:Size(max = 128, message = "Email address must be at most 128 characters.")
    @field:Email(message = "Email address must be a well-formed email address.")
    val emailAddress: String,

    @Schema(description = "Phone number with country code", example = "+48123123123")
    @field:NotBlank(message = "Phone number must not be blank.")
    @field:Size(max = 16, message = "Phone number must be at most 16 characters.")
    @field:Pattern(regexp = "^\\+?\\d+$", message = "Phone number must contain only digits and may start with +.")
    val phoneNumber: String,

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