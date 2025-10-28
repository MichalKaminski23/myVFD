package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.OperationApi
import com.vfd.client.data.remote.dtos.OperationDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OperationRepository @Inject constructor(
    private val operationApi: OperationApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

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