package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object UserDtos {

    @Serializable
    data class UserCreate(
        val firstName: String,
        val lastName: String,
        val address: AddressDtos.AddressCreate,
        val emailAddress: String,
        val phoneNumber: String,
        val password: String
    )

    @Serializable
    data class UserPatch(
        val firstName: String? = null,

        val lastName: String? = null,

        val address: AddressDtos.AddressPatch? = null,

        val emailAddress: String? = null,

        val phoneNumber: String? = null,

        val password: String? = null
    )

    @Serializable
    data class UserResponse(
        val userId: Int,

        val firstName: String,

        val lastName: String,

        val address: AddressDtos.AddressResponse,

        val emailAddress: String,

        val phoneNumber: String,

        val createdAt: LocalDateTime,

        val loggedAt: LocalDateTime,

        val active: Boolean
    )
}