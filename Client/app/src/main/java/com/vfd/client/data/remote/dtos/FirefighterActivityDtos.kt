package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object FirefighterActivityDtos {

    @Serializable
    data class FirefighterActivityCreate(
        val firefighterActivityType: String,

        val activityDate: LocalDateTime,

        val expirationDate: LocalDateTime? = null,

        val description: String? = null
    )

    @Serializable
    data class FirefighterActivityPatch(
        val firefighterActivityType: String? = null,

        val activityDate: LocalDateTime? = null,

        val expirationDate: LocalDateTime? = null,

        val description: String? = null,

        val status: String? = null
    )

    @Serializable
    data class FirefighterActivityResponse(
        val firefighterActivityId: Int,

        val firefighterId: Int,

        val firefighterActivityTypeName: String,

        val activityDate: LocalDateTime,

        val expirationDate: LocalDateTime?,

        val description: String?,

        val status: String
    )
}