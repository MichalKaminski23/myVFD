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

    @POST("api/firefighter-activities/my")
    suspend fun createFirefighterActivity(
        @Body firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse

    @GET("api/firefighter-activities/my")
    suspend fun getFirefighterActivities(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "activityDate,asc"
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>

    @PATCH("api/firefighter-activities/my/{firefighterActivityId}")
    suspend fun updateFirefighterActivity(
        @Path("firefighterActivityId") firefighterActivityId: Int,
        @Body firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse
}