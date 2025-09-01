package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.OperationTypeDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationTypeApi {

    @POST("api/operation-types")
    suspend fun createOperationType(
        @Body operationTypeDto: OperationTypeDtos.OperationTypeCreate
    ): OperationTypeDtos.OperationTypeResponse

    @GET("api/operation-types")
    suspend fun getAllOperationTypes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "operationTypeCode,asc"
    ): PageResponse<OperationTypeDtos.OperationTypeResponse>

    @GET("api/operation-types/{operationTypeCode}")
    suspend fun getOperationTypeByCode(
        @Path("operationTypeCode") operationTypeCode: String
    ): OperationTypeDtos.OperationTypeResponse

    @PATCH("api/operation-types/{operationTypeCode}")
    suspend fun updateOperationType(
        @Path("operationTypeCode") operationTypeCode: String,
        @Body operationTypePatchDto: OperationTypeDtos.OperationTypePatch
    ): OperationTypeDtos.OperationTypeResponse
}
