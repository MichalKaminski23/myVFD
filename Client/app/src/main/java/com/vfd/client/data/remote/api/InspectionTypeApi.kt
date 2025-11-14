package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.InspectionTypeDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InspectionTypeApi {

    @POST("api/inspection-types")
    suspend fun createInspectionType(
        @Body inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate
    ): InspectionTypeDtos.InspectionTypeResponse

    @GET("api/inspection-types")
    suspend fun getAllInspectionTypes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "inspectionType,asc"
    ): PageResponse<InspectionTypeDtos.InspectionTypeResponse>


    @PATCH("api/inspection-types/{inspectionTypeCode}")
    suspend fun updateInspectionType(
        @Path("inspectionTypeCode") inspectionTypeCode: String,
        @Body inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse
}