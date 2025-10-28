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
        @field:NotBlank(message = "{user.firstName.not_blank}")
        @field:Size(max = 128, message = "{user.firstName.size}")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "{user.firstName.pattern}")
        @field:Schema(description = "User's first name", example = "Super")
        val firstName: String,

        @field:NotBlank(message = "{user.lastName.not_blank}")
        @field:Size(max = 128, message = "{user.lastName.size}")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "{user.lastName.pattern}")
        @field:Schema(description = "User's last name", example = "Ziutek")
        val lastName: String,

        @field:Valid
        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressCreate,

        @field:NotBlank(message = "{user.emailAddress.not_blank}")
        @field:Size(max = 128, message = "{user.emailAddress.size}")
        @field:Email(message = "{user.emailAddress.email}")
        @field:Schema(description = "User's email address", example = "superZiutek@test.com")
        val emailAddress: String,

        @field:NotBlank(message = "{user.phoneNumber.not_blank}")
        @field:Size(min = 9, max = 16, message = "{user.phoneNumber.size}")
        @field:Pattern(
            regexp = "^\\+?\\d+$",
            message = "{user.phoneNumber.pattern}"
        )
        @field:Schema(description = "User's phone number", example = "+48123123123")
        val phoneNumber: String,

        @param:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "{password.new.not_blank}")
        @field:Size(min = 8, max = 128, message = "{password.new.size}")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "{password.new.pattern}"
        )
        @field:Schema(description = "User's password", example = "Dupa12345!")
        val password: String
    )

    @Schema(description = "DTO used for logging in a user")
    data class UserLogin(
        @field:NotBlank(message = "{user.emailAddress.not_blank}")
        @field:Size(max = 128, message = "{user.emailAddress.size}")
        @field:Email(message = "{user.emailAddress.email}")
        @field:Schema(description = "User's email address", example = "jan.kowalski@test.com")
        val emailAddress: String,

        @param:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:NotBlank(message = "{password.new.not_blank}")
        @field:Size(min = 8, max = 128, message = "{password.new.size}")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "{password.new.pattern}"
        )
        @field:Schema(description = "User's password", example = "Dupa12345!")
        val password: String
    )

    @Schema(description = "DTO used for partially updating a user")
    data class UserPatch(
        @field:Size(max = 128, message = "{user.firstName.size}")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "{user.firstName.pattern}")
        @field:Schema(description = "User's first name", example = "Super")
        val firstName: String? = null,

        @field:Size(max = 128, message = "{user.lastName.size}")
        @field:Pattern(regexp = "^[A-Za-z]+$", message = "{user.lastName.pattern}")
        @field:Schema(description = "User's last name", example = "Ziutek")
        val lastName: String? = null,

        @field:Valid
        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressCreate? = null,

        @field:Email(message = "{user.emailAddress.email}")
        @field:Size(max = 128, message = "{user.emailAddress.size}")
        @field:Schema(description = "User's email address", example = "superZiutek@test.com")
        val emailAddress: String? = null,

        @field:Size(max = 16, message = "{user.phoneNumber.size}")
        @field:Pattern(regexp = "^\\+?\\d+$", message = "{user.phoneNumber.pattern}")
        @field:Schema(description = "User's phone number", example = "+48123123123")
        val phoneNumber: String? = null,

        @param:JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @field:Size(min = 8, max = 128, message = "{password.new.size}")
        @field:Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$",
            message = "{password.new.pattern}"
        )
        @field:Schema(description = "User's password", example = "Dupa12345!")
        val password: String? = null
    )

    @Schema(description = "DTO used for returning user information for user")
    data class UserResponse(
        @field:Schema(description = "User ID", example = "7")
        val userId: Int,

        @field:Schema(description = "User's first name", example = "Super")
        val firstName: String,

        @field:Schema(description = "User's last name", example = "Ziutek")
        val lastName: String,

        @field:Schema(description = "User's address")
        val address: AddressDtos.AddressResponse,

        @field:Schema(description = "User's email address", example = "superZiutek@test.com")
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

        @field:Schema(description = "User's first name", example = "Super")
        val firstName: String,

        @field:Schema(description = "User's last name", example = "Ziutek")
        val lastName: String,

        @field:Schema(description = "Email address", example = "superZiutek@test.com")
        val emailAddress: String,

        @field:Schema(description = "Phone number", example = "+48123123123")
        val phoneNumber: String,

        @field:Schema(description = "Indicates whether the user is active", example = "true")
        val active: Boolean
    )
}