package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object FirefighterActivityDtos {

    @Serializable
    data class FirefighterActivityCreate(
        val firefighterId: Int,

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

        val description: String? = null
    )

    @Serializable
    data class FirefighterActivityResponse(
        val firefighterActivityId: Int,

        val firefighter: FirefighterDtos.FirefighterResponse,

        val firefighterActivityType: FirefighterActivityTypeDtos.FirefighterActivityTypeResponse,

        val activityDate: LocalDateTime,

        val expirationDate: LocalDateTime?,

        val description: String?
    )
}