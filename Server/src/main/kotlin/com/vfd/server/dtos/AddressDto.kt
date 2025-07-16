package com.vfd.server.dtos

data class AddressDto(
    val addressId: Int?,
    val country: String,
    val voivodeship: String,
    val city: String,
    val postalCode: String,
    val street: String,
    val houseNumber: String?,
    val apartNumber: String?
)