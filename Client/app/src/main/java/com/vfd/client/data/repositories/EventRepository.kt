package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.EventApi
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventApi: EventApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createEvent(eventDto: EventDtos.EventCreate): ApiResult<EventDtos.EventResponse> =
        safeApiCall { eventApi.createEvent(eventDto) }

    suspend fun getAllEvents(
        page: Int = 0,
        size: Int = 20,
        sort: String = "eventId,asc"
    ): ApiResult<PageResponse<EventDtos.EventResponse>> =
        safeApiCall { eventApi.getAllEvents(page, size, sort) }

    suspend fun getEventById(eventId: Int): ApiResult<EventDtos.EventResponse> =
        safeApiCall { eventApi.getEventById(eventId) }

    suspend fun updateEvent(
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): ApiResult<EventDtos.EventResponse> =
        safeApiCall { eventApi.updateEvent(eventId, eventDto) }
}