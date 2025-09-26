package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.OperationApi
import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val operationApi: OperationApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createOperation(
        operationDto: OperationDtos.OperationCreate
    ): ApiResult<OperationDtos.OperationResponse> =
        safeApiCall { operationApi.createOperation(operationDto) }

    suspend fun getOperations(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationDate,desc"
    ): ApiResult<PageResponse<OperationDtos.OperationResponse>> =
        safeApiCall { operationApi.getOperations(page, size, sort) }

    suspend fun updateOperation(
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): ApiResult<OperationDtos.OperationResponse> =
        safeApiCall { operationApi.updateOperation(operationId, operationDto) }
}