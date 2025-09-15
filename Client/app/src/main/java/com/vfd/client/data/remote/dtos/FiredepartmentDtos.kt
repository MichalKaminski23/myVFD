package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object FiredepartmentDtos {

    @Serializable
    data class FiredepartmentCreate(
        val name: String,

        val address: AddressDtos.AddressCreate,

        val nrfs: Boolean = true
    )

    @Serializable
    data class FiredepartmentPatch(
        val name: String? = null,

//        val address: AddressDtos.AddressPatch? = null,

        val nrfs: Boolean? = null
    )

    @Serializable
    data class FiredepartmentResponse(
        val firedepartmentId: Int,

        val name: String,

        val address: AddressDtos.AddressResponse,

        val nrfs: Boolean
    )
}