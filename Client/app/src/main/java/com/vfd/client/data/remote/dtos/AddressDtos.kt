package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object AddressDtos {

    @Serializable
    data class AddressCreate(
        val country: String,

        val voivodeship: String,

        val city: String,

        val postalCode: String,

        val street: String,

        val houseNumber: String,

        val apartNumber: String? = null
    )

    @Serializable
    data class AddressResponse(
        val addressId: Int,

        val country: String,

        val voivodeship: String,

        val city: String,

        val postalCode: String,

        val street: String,

        val houseNumber: String,

        val apartNumber: String?
    )
}