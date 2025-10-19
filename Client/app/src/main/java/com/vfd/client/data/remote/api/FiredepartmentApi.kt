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

    @POST("api/firedepartments/admin")
    suspend fun createFiredepartment(
        @Body firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse

    @GET("api/firedepartments")
    suspend fun getFiredepartmentsShort(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "name,asc"
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponseShort>

    @GET("api/firedepartments/all")
    suspend fun getFiredepartments(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "name,asc"
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponse>

    @GET("api/firedepartments/my")
    suspend fun getFiredepartment(): FiredepartmentDtos.FiredepartmentResponse

    @PATCH("api/firedepartments/{firedepartmentId}")
    suspend fun updateFiredepartment(
        @Path("firedepartmentId") firedepartmentId: Int,
        @Body firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse
}