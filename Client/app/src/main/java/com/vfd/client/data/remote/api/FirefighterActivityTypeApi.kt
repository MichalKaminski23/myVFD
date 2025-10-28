package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FirefighterActivityTypeApi {

    @POST("api/firefighter-activity-types")
    suspend fun createFirefighterActivityType(
        @Body firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

    @GET("api/firefighter-activity-types")
    suspend fun getAllFirefighterActivityTypes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "firefighterActivityType,asc"
    ): PageResponse<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse>

//    @GET("api/firefighter-activity-types/{firefighterActivityTypeCode}")
//    suspend fun getFirefighterActivityTypeByCode(
//        @Path("firefighterActivityTypeCode") firefighterActivityTypeCode: String
//    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

    @PATCH("api/firefighter-activity-types/{firefighterActivityTypeCode}")
    suspend fun updateFirefighterActivityType(
        @Path("firefighterActivityTypeCode") firefighterActivityTypeCode: String,
        @Body firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse
}