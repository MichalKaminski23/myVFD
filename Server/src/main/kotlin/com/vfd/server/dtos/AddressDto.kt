package com.vfd.server.dtos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AddressDto(
    val addressId: Int? = null,

    @field:NotBlank(message = "Country must not be blank.")
    @field:Size(max = 64, message = "Country must be at most 64 characters.")
    val country: String,

    @field:NotBlank(message = "Voivodeship must not be blank.")
    @field:Size(max = 64, message = "Voivodeship must be at most 64 characters.")
    val voivodeship: String,

    @field:NotBlank(message = "City must not be blank.")
    @field:Size(max = 64, message = "City must be at most 64 characters.")
    val city: String,

    @field:NotBlank(message = "PostalCode must not be blank.")
    @field:Size(max = 16, message = "PostalCode must be at most 16 characters.")
    val postalCode: String,

    @field:NotBlank(message = "Street must not be blank.")
    @field:Size(max = 64, message = "Street must be at most 64 characters.")
    val street: String,

    @field:Size(max = 8, message = "HouseNumber must be at most 8 characters.")
    val houseNumber: String? = null,

    @field:Size(max = 8, message = "ApartNumber must be at most 8 characters.")
    val apartNumber: String? = null
)