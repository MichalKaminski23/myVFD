package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

object AddressDtos {

    @Schema(description = "DTO used for creating a new address")
    data class AddressCreate(
        @field:NotBlank(message = "Country must not be blank.")
        @field:Size(max = 64, message = "Country name must be at most 64 characters.")
        @field:Schema(description = "Country name", example = "Poland")
        val country: String,

        @field:NotBlank(message = "Voivodeship must not be blank.")
        @field:Size(max = 64, message = "Voivodeship name must be at most 64 characters.")
        @field:Schema(description = "Voivodeship or province", example = "Silesian")
        val voivodeship: String,

        @field:NotBlank(message = "City must not be blank.")
        @field:Size(max = 64, message = "City name must be at most 64 characters.")
        @field:Schema(description = "City name", example = "Strzyzowice")
        val city: String,

        @field:NotBlank(message = "Postal code must not be blank.")
        @field:Size(max = 16, message = "Postal code must be at most 16 characters.")
        @field:Schema(description = "Postal code", example = "69-420")
        @field:Pattern(
            regexp = "^\\d{2,3}-\\d{2,3}$",
            message = "Postal code must be 2–3 digits, a hyphen, then 2–3 digits (e.g., 12-34 or 123-45)."
        )
        val postalCode: String,

        @field:NotBlank(message = "Street must not be blank.")
        @field:Size(max = 64, message = "Street name must be at most 64 characters.")
        @field:Schema(description = "Street name", example = "Belna")
        val street: String,

        @field:NotBlank(message = "House number must not be blank.")
        @field:Size(max = 8, message = "House number must be at most 8 characters.")
        @field:Schema(description = "House number", example = "1")
        val houseNumber: String,

        @field:Size(max = 8, message = "Apartment number must be at most 8 characters.")
        @field:Schema(description = "Apartment number", example = "7")
        val apartNumber: String? = null
    )

    @Schema(description = "DTO used for returning address information")
    data class AddressResponse(
        @field:Schema(description = "Unique identifier of the address", example = "5")
        val addressId: Int,

        @field:Schema(description = "Country name", example = "Poland")
        val country: String,

        @field:Schema(description = "Voivodeship or province", example = "Silesian")
        val voivodeship: String,

        @field:Schema(description = "City name", example = "Strzyzowice")
        val city: String,

        @field:Schema(description = "Postal code", example = "69-420")
        val postalCode: String,

        @field:Schema(description = "Street name", example = "Belna")
        val street: String,

        @field:Schema(description = "House number", example = "1")
        val houseNumber: String,

        @field:Schema(description = "Apartment number", example = "7")
        val apartNumber: String?
    )
}