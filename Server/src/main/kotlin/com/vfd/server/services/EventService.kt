package com.vfd.server.services

import com.vfd.server.dtos.EventDtos
import com.vfd.server.shared.PageResponse

interface EventService {

    fun createEvent(emailAddress: String, eventDto: EventDtos.EventCreate): EventDtos.EventResponse

    fun getEvents(
        page: Int = 0,
        size: Int = 20,
        sort: String = "eventDate,desc",
        emailAddress: String
    ): PageResponse<EventDtos.EventResponse>

    fun updateEvent(
        emailAddress: String,
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse

    fun createEventDev(eventDto: EventDtos.EventCreateDev): EventDtos.EventResponse

    fun getAllEventsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "eventDate,desc"
    ): PageResponse<EventDtos.EventResponse>

    fun getEventByIdDev(eventId: Int): EventDtos.EventResponse

    fun updateEventDev(
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse
}