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
    data class AddressPatch(
        val country: String? = null,

        val voivodeship: String? = null,

        val city: String? = null,

        val postalCode: String? = null,

        val street: String? = null,

        val houseNumber: String? = null,

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