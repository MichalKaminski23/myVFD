package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.FirefighterApi
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FirefighterRepository @Inject constructor(
    private val firefighterApi: FirefighterApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createFirefighter(
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.createFirefighter(firefighterDto) }

    suspend fun getAllFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterId,asc"
    ): ApiResult<PageResponse<FirefighterDtos.FirefighterResponse>> =
        safeApiCall { firefighterApi.getAllFirefighters(page, size, sort) }

    suspend fun getFirefighterById(
        firefighterId: Int
    ): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.getFirefighterById(firefighterId) }

    suspend fun updateFirefighter(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.updateFirefighter(firefighterId, firefighterDto) }

    suspend fun getCurrentFirefighter(): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.getCurrentFirefighter() }

    suspend fun getPendingFirefighters(): ApiResult<List<FirefighterDtos.FirefighterResponse>> =
        safeApiCall { firefighterApi.getPendingFirefighters() }
}