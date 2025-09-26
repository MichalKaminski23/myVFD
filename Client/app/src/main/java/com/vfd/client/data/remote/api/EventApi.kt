package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.EventDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @POST("api/events/my")
    suspend fun createEvent(
        @Body eventDto: EventDtos.EventCreate
    ): EventDtos.EventResponse

    @GET("api/events/my")
    suspend fun getEvents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "eventDate,desc"
    ): PageResponse<EventDtos.EventResponse>

    @PATCH("api/events/my/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: Int,
        @Body eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse
}