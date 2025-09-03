package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.InspectionTypeApi
import com.vfd.client.data.remote.dtos.InspectionTypeDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InspectionTypeRepository @Inject constructor(
    private val inspectionTypeApi: InspectionTypeApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createInspectionType(
        inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate
    ): ApiResult<InspectionTypeDtos.InspectionTypeResponse> =
        safeApiCall { inspectionTypeApi.createInspectionType(inspectionTypeDto) }

    suspend fun getAllInspectionTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionType,asc"
    ): ApiResult<PageResponse<InspectionTypeDtos.InspectionTypeResponse>> =
        safeApiCall { inspectionTypeApi.getAllInspectionTypes(page, size, sort) }

    suspend fun getInspectionTypeByCode(
        inspectionTypeCode: String
    ): ApiResult<InspectionTypeDtos.InspectionTypeResponse> =
        safeApiCall { inspectionTypeApi.getInspectionTypeByCode(inspectionTypeCode) }

    suspend fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): ApiResult<InspectionTypeDtos.InspectionTypeResponse> =
        safeApiCall {
            inspectionTypeApi.updateInspectionType(
                inspectionTypeCode,
                inspectionTypeDto
            )
        }
}