package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.FiredepartmentApi
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FiredepartmentRepository @Inject constructor(
    private val firedepartmentApi: FiredepartmentApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createFiredepartment(firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate): ApiResult<FiredepartmentDtos.FiredepartmentResponse> =
        safeApiCall { firedepartmentApi.createFiredepartment(firedepartmentDto) }

    suspend fun getAllFiredepartments(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firedepartmentId,asc"
    ): ApiResult<PageResponse<FiredepartmentDtos.FiredepartmentResponse>> =
        safeApiCall { firedepartmentApi.getAllFiredepartments(page, size, sort) }

    suspend fun getFiredepartmentById(firedepartmentId: Int): ApiResult<FiredepartmentDtos.FiredepartmentResponse> =
        safeApiCall { firedepartmentApi.getFiredepartmentById(firedepartmentId) }

    suspend fun updateFiredepartment(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): ApiResult<FiredepartmentDtos.FiredepartmentResponse> =
        safeApiCall { firedepartmentApi.updateFiredepartment(firedepartmentId, firedepartmentDto) }
}