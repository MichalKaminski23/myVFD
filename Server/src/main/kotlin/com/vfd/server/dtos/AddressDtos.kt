package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

object AddressDtos {

    @Schema(description = "DTO used for creating a new address")
    data class AddressCreate(
        @field:NotBlank(message = "{address.country.not_blank}")
        @field:Size(max = 64, message = "{address.country.size}")
        @field:Schema(description = "Country name", example = "Polska")
        val country: String,

        @field:NotBlank(message = "{address.voivodeship.not_blank}")
        @field:Size(max = 64, message = "{address.voivodeship.size}")
        @field:Schema(description = "Voivodeship or province", example = "Slaskie")
        val voivodeship: String,

        @field:NotBlank(message = "{address.city.not_blank}")
        @field:Size(max = 64, message = "{address.city.size}")
        @field:Schema(description = "City name", example = "Strzyzowice")
        val city: String,

        @field:NotBlank(message = "{address.postalCode.not_blank}")
        @field:Size(max = 16, message = "{address.postalCode.size}")
        @field:Schema(description = "Postal code", example = "69-420")
        @field:Pattern(
            regexp = "^\\d{2,3}-\\d{2,3}$",
            message = "{address.postalCode.pattern}"
        )
        val postalCode: String,

        @field:NotBlank(message = "{address.street.not_blank}")
        @field:Size(max = 64, message = "{address.street.size}")
        @field:Schema(description = "Street name", example = "Belna")
        val street: String,

        @field:NotBlank(message = "{address.houseNumber.not_blank}")
        @field:Size(max = 8, message = "{address.houseNumber.size}")
        @field:Schema(description = "House number", example = "1")
        val houseNumber: String,

        @field:Size(max = 8, message = "{address.apartNumber.size}")
        @field:Schema(description = "Apartment number", example = "7")
        val apartNumber: String? = null
    )

    @Schema(description = "DTO used for returning address information")
    data class AddressResponse(
        @field:Schema(description = "Unique identifier of the address", example = "5")
        val addressId: Int,

        @field:Schema(description = "Country name", example = "Polska")
        val country: String,

        @field:Schema(description = "Voivodeship or province", example = "Slaskie")
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