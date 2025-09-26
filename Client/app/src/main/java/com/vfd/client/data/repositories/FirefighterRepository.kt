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

    suspend fun getFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "user.firstName,asc"
    ): ApiResult<PageResponse<FirefighterDtos.FirefighterResponse>> =
        safeApiCall { firefighterApi.getFirefighters(page, size, sort) }

    suspend fun updateFirefighter(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.updateFirefighter(firefighterId, firefighterDto) }

    suspend fun getFirefighterByEmailAddress(): ApiResult<FirefighterDtos.FirefighterResponse> =
        safeApiCall { firefighterApi.getFirefighterByEmailAddress() }

    suspend fun getPendingFirefighters(
        page: Int = 0,
        size: Int = 20,
        sort: String = "user.firstName,asc"
    ): ApiResult<PageResponse<FirefighterDtos.FirefighterResponse>> =
        safeApiCall { firefighterApi.getPendingFirefighters(page, size, sort) }
}