package com.vfd.server.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

object UserDtos {

    @Schema(description = "DTO used for registering or creating a new user")
    data class UserCreate(
        @field:NotBlank(message = "First name must not be blank.")
        @field:Size(max = 128, message = "First name must bet at most 128 characters.")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters.")
        @field:Schema(description = "User's first name", example = "Arkadiusz")
        val firstName: String,

        @field:NotBlank(message = "Last name must not be blank.")
        @field:Size(max = 128, message = "Last name must be at most 128 characters.")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters.")
        @field:Schema(description = "User's last name", example = "Niemusialski")
        val lastName: String,

        @field:Valid
        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressCreate,

        @field:NotBlank(message = "Name must not be blank.")
        @field:Size(max = 128, message = "Email address must be at most 128 characters.")
        @field:Email(message = "Email must be a valid email address.")
        @field:Schema(description = "User's email address", example = "arek.kozak@test.com")
        val emailAddress: String,

        @field:NotBlank(message = "Phone number must not be blank.")
        @field:Size(max = 16, message = "Phone number must be at most 16 characters.")
        @field:Pattern(regexp = "^\\+?\\d+$")
        @field:Schema(description = "User's phone number", example = "+48123123123")
        val phoneNumber: String,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "Password must not be blank.")
        @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
        )
        @field:Schema(description = "User's password", example = "P@ssw0rd123!")
        val password: String
    )

    @Schema(description = "DTO used for logging in a user")
    data class UserLogin(
        @field:NotBlank(message = "Email address must not be blank.")
        @field:Size(max = 128, message = "Email address must be at most 128 characters.")
        @field:Email(message = "Email must be a valid email address.")
        @field:Schema(description = "User's email address", example = "arek.kozak@test.com")
        val emailAddress: String,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "Password must not be blank.")
        @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
        )
        @field:Schema(description = "User's password", example = "P@ssw0rd123!")
        val password: String
    )

    @Schema(description = "DTO used for partially updating a user")
    data class UserPatch(
        @field:Size(max = 128, message = "First name must be at most 128 characters.")
        @field:Pattern(regexp = "^[A-Za-z]+$")
        @field:Schema(description = "User's first name", example = "Arkadiusz")
        val firstName: String? = null,

        @field:Size(max = 128, message = "Last name must be at most 128 characters.")
        @field:Pattern(regexp = "^[A-Za-z]+$")
        @field:Schema(description = "User's last name", example = "Niemusialski")
        val lastName: String? = null,

        @field:Valid
        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressPatch? = null,

        @field:Email(message = "Email must be a valid email address.")
        @field:Size(max = 128, message = "Email address must be at most 128 characters.")
        @field:Schema(description = "User's email address", example = "arek.kozak@test.com")
        val emailAddress: String? = null,

        @field:Size(max = 16, message = "Phone number must be at most 16 characters.")
        @field:Pattern(regexp = "^\\+?\\d+$")
        @field:Schema(description = "User's phone number", example = "+48123123123")
        val phoneNumber: String? = null,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "Password must contain at least one uppercase letter, one digit, and one special character."
        )
        @field:Schema(description = "User's password", example = "P@ssw0rd123!")
        val password: String? = null
    )

    @Schema(description = "DTO used for returning user information for user")
    data class UserResponse(
        @field:Schema(description = "User ID", example = "7")
        val userId: Int,

        @field:Schema(description = "User's first name", example = "Arek")
        val firstName: String,

        @field:Schema(description = "User's last name", example = "Niemusialski")
        val lastName: String,

        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressResponse,

        @field:Schema(description = "Email address", example = "arek.kozak@test.com")
        val emailAddress: String,

        @field:Schema(description = "Phone number", example = "+48123123123")
        val phoneNumber: String,

        @field:Schema(description = "Timestamp of registration", example = "2025-08-03T14:00:00")
        val createdAt: LocalDateTime,

        @field:Schema(description = "Last login timestamp", example = "2025-08-05T08:45:00")
        val loggedAt: LocalDateTime,

        @field:Schema(description = "Indicates whether the user is active", example = "true")
        val active: Boolean
    )

    @Schema(description = "DTO used for returning user information for moderator")
    data class UserModeratorResponse(
        @field:Schema(description = "User ID", example = "7")
        val userId: Int,

        @field:Schema(description = "User's first name", example = "Arek")
        val firstName: String,

        @field:Schema(description = "User's last name", example = "Niemusialski")
        val lastName: String,

        @field:Schema(description = "Email address", example = "arek.kozak@test.com")
        val emailAddress: String,

        @field:Schema(description = "Phone number", example = "+48123123123")
        val phoneNumber: String,

        @field:Schema(description = "Indicates whether the user is active", example = "true")
        val active: Boolean
    )
}