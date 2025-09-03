package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.FirefighterActivityApi
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FirefighterActivityRepository @Inject constructor(
    private val firefighterActivityApi: FirefighterActivityApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createFirefighterActivity(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate): ApiResult<FirefighterActivityDtos.FirefighterActivityResponse> =
        safeApiCall { firefighterActivityApi.createFirefighterActivity(firefighterActivityDto) }

    suspend fun getAllFirefighterActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityId,asc"
    ): ApiResult<PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>> =
        safeApiCall { firefighterActivityApi.getAllFirefighterActivities(page, size, sort) }

    suspend fun getFirefighterActivityById(firefighterActivityId: Int): ApiResult<FirefighterActivityDtos.FirefighterActivityResponse> =
        safeApiCall { firefighterActivityApi.getFirefighterActivityById(firefighterActivityId) }

    suspend fun updateFirefighterActivity(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): ApiResult<FirefighterActivityDtos.FirefighterActivityResponse> =
        safeApiCall {
            firefighterActivityApi.updateFirefighterActivity(
                firefighterActivityId,
                firefighterActivityDto
            )
        }
}