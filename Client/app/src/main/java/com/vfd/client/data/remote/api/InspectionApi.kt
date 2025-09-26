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

    @POST("api/inspections/my")
    suspend fun createInspection(
        @Body inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse

    @GET("api/inspections/my")
    suspend fun getInspections(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "inspectionDate,desc"
    ): PageResponse<InspectionDtos.InspectionResponse>

    @PATCH("api/inspections/my/{inspectionId}")
    suspend fun updateInspection(
        @Path("inspectionId") inspectionId: Int,
        @Body inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse
}