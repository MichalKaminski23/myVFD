package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object EventDtos {

    @Serializable
    data class EventCreate(
        val firedepartmentId: Int,

        val header: String,

        val description: String,

        val eventDate: LocalDateTime
    )

    @Serializable
    data class EventPatch(
        val header: String? = null,

        val description: String? = null,

        val eventDate: LocalDateTime? = null
    )

    @Serializable
    data class EventResponse(
        val eventId: Int,

        val firedepartmentId: Int,

        val header: String,

        val description: String,

        val eventDate: LocalDateTime
    )
}