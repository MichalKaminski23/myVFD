package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.InspectionApi
import com.vfd.client.data.remote.dtos.InspectionDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InspectionRepository @Inject constructor(
    private val inspectionApi: InspectionApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createInspection(
        inspectionDto: InspectionDtos.InspectionCreate
    ): ApiResult<InspectionDtos.InspectionResponse> =
        safeApiCall { inspectionApi.createInspection(inspectionDto) }

    suspend fun getInspections(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionDate,asc"
    ): ApiResult<PageResponse<InspectionDtos.InspectionResponse>> =
        safeApiCall { inspectionApi.getInspections(page, size, sort) }

    suspend fun updateInspection(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): ApiResult<InspectionDtos.InspectionResponse> =
        safeApiCall { inspectionApi.updateInspection(inspectionId, inspectionDto) }
}