package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.OperationTypeApi
import com.vfd.client.data.remote.dtos.OperationTypeDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class OperationTypeRepository @Inject constructor(
    private val operationTypeApi: OperationTypeApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun createOperationType(
        operationTypeDto: OperationTypeDtos.OperationTypeCreate
    ): ApiResult<OperationTypeDtos.OperationTypeResponse> =
        safeApiCall { operationTypeApi.createOperationType(operationTypeDto) }

    suspend fun getAllOperationTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationType,asc"
    ): ApiResult<PageResponse<OperationTypeDtos.OperationTypeResponse>> =
        safeApiCall { operationTypeApi.getAllOperationTypes(page, size, sort) }

//    suspend fun getOperationTypeByCode(
//        operationTypeCode: String
//    ): ApiResult<OperationTypeDtos.OperationTypeResponse> =
//        safeApiCall { operationTypeApi.getOperationTypeByCode(operationTypeCode) }

    suspend fun updateOperationType(
        operationTypeCode: String,
        operationTypeDto: OperationTypeDtos.OperationTypePatch
    ): ApiResult<OperationTypeDtos.OperationTypeResponse> =
        safeApiCall { operationTypeApi.updateOperationType(operationTypeCode, operationTypeDto) }
}