package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FirefighterActivityApi {

    @POST("api/firefighter-activities")
    suspend fun createFirefighterActivity(
        @Body firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse

    @GET("api/firefighter-activities")
    suspend fun getAllFirefighterActivities(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "firefighterActivityId,asc"
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>

    @GET("api/firefighter-activities/{firefighterActivityId}")
    suspend fun getFirefighterActivityById(
        @Path("firefighterActivityId") firefighterActivityId: Int
    ): FirefighterActivityDtos.FirefighterActivityResponse

    @PATCH("api/firefighter-activities/{firefighterActivityId}")
    suspend fun updateFirefighterActivity(
        @Path("firefighterActivityId") firefighterActivityId: Int,
        @Body firefighterActivityPatchDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse
}
