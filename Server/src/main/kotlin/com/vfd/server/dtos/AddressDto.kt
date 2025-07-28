package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO representing an address.")
data class AddressDto(
    @Schema(
        description = "Unique identifier of the address",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val addressId: Int? = null,

    @Schema(description = "Country name", example = "Poland")
    @field:NotBlank(message = "Country must not be blank.")
    @field:Size(max = 64, message = "Country must be at most 64 characters.")
    val country: String,

    @Schema(description = "Voivodeship (province)", example = "Silesia")
    @field:NotBlank(message = "Voivodeship must not be blank.")
    @field:Size(max = 64, message = "Voivodeship must be at most 64 characters.")
    val voivodeship: String,

    @Schema(description = "City name", example = "Katowice")
    @field:NotBlank(message = "City must not be blank.")
    @field:Size(max = 64, message = "City must be at most 64 characters.")
    val city: String,

    @Schema(description = "Postal code", example = "69-420")
    @field:NotBlank(message = "Postal code must not be blank.")
    @field:Size(max = 16, message = "Postal code must be at most 16 characters.")
    val postalCode: String,

    @Schema(description = "Street name", example = "Mariacka")
    @field:NotBlank(message = "Street must not be blank.")
    @field:Size(max = 64, message = "Street must be at most 64 characters.")
    val street: String,

    @Schema(description = "House number", example = "10A")
    @field:Size(max = 8, message = "House number must be at most 8 characters.")
    val houseNumber: String? = null,

    @Schema(description = "Apartment number", example = "5")
    @field:Size(max = 8, message = "Apart number must be at most 8 characters.")
    val apartNumber: String? = null
)