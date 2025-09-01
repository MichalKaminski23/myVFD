package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.InspectionDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InspectionApi {

    @POST("api/inspections")
    suspend fun createInspection(
        @Body inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse

    @GET("api/inspections")
    suspend fun getAllInspections(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "inspectionId,asc"
    ): PageResponse<InspectionDtos.InspectionResponse>

    @GET("api/inspections/{inspectionId}")
    suspend fun getInspectionById(
        @Path("inspectionId") inspectionId: Int
    ): InspectionDtos.InspectionResponse

    @PATCH("api/inspections/{inspectionId}")
    suspend fun updateInspection(
        @Path("inspectionId") inspectionId: Int,
        @Body inspectionPatchDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse
}