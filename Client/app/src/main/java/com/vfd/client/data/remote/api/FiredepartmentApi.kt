package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FiredepartmentApi {

    @POST("api/firedepartments")
    suspend fun createFiredepartment(
        @Body firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse

    @GET("api/firedepartments")
    suspend fun getAllFiredepartments(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "firedepartmentId,asc"
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponse>

    @GET("api/firedepartments/{firedepartmentId}")
    suspend fun getFiredepartmentById(
        @Path("firedepartmentId") firedepartmentId: Int
    ): FiredepartmentDtos.FiredepartmentResponse

    @PATCH("api/firedepartments/{firedepartmentId}")
    suspend fun updateFiredepartment(
        @Path("firedepartmentId") firedepartmentId: Int,
        @Body firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse
}