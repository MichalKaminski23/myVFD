package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.EventApi
import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val eventApi: EventApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun createEvent(eventDto: EventDtos.EventCreate): ApiResult<EventDtos.EventResponse> =
        safeApiCall { eventApi.createEvent(eventDto) }

    suspend fun getEvents(
        page: Int = 0,
        size: Int = 20,
        sort: String = "eventDate,desc"
    ): ApiResult<PageResponse<EventDtos.EventResponse>> =
        safeApiCall { eventApi.getEvents(page, size, sort) }

    suspend fun updateEvent(
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): ApiResult<EventDtos.EventResponse> =
        safeApiCall { eventApi.updateEvent(eventId, eventDto) }
}