package com.vfd.server.services

import com.vfd.server.dtos.EventDtos
import com.vfd.server.shared.PageResponse

interface EventService {

    fun createEvent(eventDto: EventDtos.EventCreate): EventDtos.EventResponse

    fun getAllEvents(
        page: Int = 0,
        size: Int = 20,
        sort: String = "eventId,asc"
    ): PageResponse<EventDtos.EventResponse>

    fun getEventById(eventId: Int): EventDtos.EventResponse

    fun updateEvent(
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse
}