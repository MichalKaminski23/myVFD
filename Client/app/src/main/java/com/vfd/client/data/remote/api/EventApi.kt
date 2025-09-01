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

    @POST("api/events")
    suspend fun createEvent(
        @Body eventDto: EventDtos.EventCreate
    ): EventDtos.EventResponse

    @GET("api/events")
    suspend fun getAllEvents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "eventId,asc"
    ): PageResponse<EventDtos.EventResponse>

    @GET("api/events/{eventId}")
    suspend fun getEventById(
        @Path("eventId") eventId: Int
    ): EventDtos.EventResponse

    @PATCH("api/events/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: Int,
        @Body eventPatchDto: EventDtos.EventPatch
    ): EventDtos.EventResponse
}