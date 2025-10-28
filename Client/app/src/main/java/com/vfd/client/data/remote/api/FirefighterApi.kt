package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.HoursResponseDto
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FirefighterApi {

    @POST("api/firefighters/my")
    suspend fun createFirefighter(
        @Body firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse

    @POST("api/firefighters/admin")
    suspend fun createFirefighterByEmailAddress(
        @Body firefighterDto: FirefighterDtos.FirefighterCreateByEmailAddress
    ): FirefighterDtos.FirefighterResponse

    @GET("api/firefighters/my")
    suspend fun getFirefighters(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "user.firstName,asc"
    ): PageResponse<FirefighterDtos.FirefighterResponse>

    @PATCH("api/firefighters/my/{firefighterId}")
    suspend fun updateFirefighter(
        @Path("firefighterId") firefighterId: Int,
        @Body firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse

    @GET("api/firefighters/me")
    suspend fun getFirefighterByEmailAddress(): FirefighterDtos.FirefighterResponse

    @GET("api/firefighters/my/pending")
    suspend fun getPendingFirefighters(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "user.firstName,asc"
    ): PageResponse<FirefighterDtos.FirefighterResponse>

    @DELETE("api/firefighters/my/{firefighterId}")
    suspend fun deleteFirefighter(
        @Path("firefighterId") firefighterId: Int
    )

    @GET("api/firefighters/my/hours")
    suspend fun getHoursForQuarter(
        @Query("year") year: Int,
        @Query("quarter") quarter: Int
    ): HoursResponseDto
}