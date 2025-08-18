package com.vfd.client

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)

data class AddressTestDto(
    val addressId: Int,

    val country: String,

    val voivodeship: String,

    val city: String,

    val postalCode: String,

    val street: String,

    val houseNumber: String,

    val apartNumber: String?
)