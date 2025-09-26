package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object FirefighterDtos {

    @Serializable
    data class FirefighterCreate(
        val userId: Int,

        val firedepartmentId: Int,
    )

    @Serializable
    data class FirefighterPatch(
        val role: String? = null,

        val status: String? = null
    )

    @Serializable
    data class FirefighterResponse(
        val firefighterId: Int,

        val firstName: String,

        val lastName: String,

        val emailAddress: String,

        val userId: Int,

        val firedepartmentId: Int,

        val firedepartmentName: String,

        val role: String,

        val status: String
    )

    @Serializable
    data class FirefighterResponseShort(
        val firefighterId: Int,

        val firstName: String,

        val lastName: String,

        val emailAddress: String
    )
}