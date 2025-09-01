package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationApi {

    @POST("api/operations")
    suspend fun createOperation(
        @Body operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse

    @GET("api/operations")
    suspend fun getAllOperations(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "operationId,asc"
    ): PageResponse<OperationDtos.OperationResponse>

    @GET("api/operations/{operationId}")
    suspend fun getOperationById(
        @Path("operationId") operationId: Int
    ): OperationDtos.OperationResponse

    @PATCH("api/operations/{operationId}")
    suspend fun updateOperation(
        @Path("operationId") operationId: Int,
        @Body operationPatchDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse
}