package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object FirefighterActivityTypeDtos {

    @Serializable
    data class FirefighterActivityTypeCreate(
        val firefighterActivityType: String,

        val name: String
    )

    @Serializable
    data class FirefighterActivityTypePatch(
        val name: String? = null
    )

    @Serializable
    data class FirefighterActivityTypeResponse(
        val firefighterActivityType: String,

        val name: String
    )
}