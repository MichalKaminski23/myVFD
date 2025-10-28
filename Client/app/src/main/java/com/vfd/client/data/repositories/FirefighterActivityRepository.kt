package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.FirefighterActivityApi
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FirefighterActivityRepository @Inject constructor(
    private val firefighterActivityApi: FirefighterActivityApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun createFirefighterActivity(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate): ApiResult<FirefighterActivityDtos.FirefighterActivityResponse> =
        safeApiCall { firefighterActivityApi.createFirefighterActivity(firefighterActivityDto) }

    suspend fun getFirefighterActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "activityDate,asc"
    ): ApiResult<PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>> =
        safeApiCall { firefighterActivityApi.getFirefighterActivities(page, size, sort) }

    suspend fun getFirefightersActivities(
        page: Int = 0,
        size: Int = 20,
        sort: String = "activityDate,asc"
    ): ApiResult<PageResponse<FirefighterActivityDtos.FirefighterActivityResponse>> =
        safeApiCall { firefighterActivityApi.getFirefightersActivities(page, size, sort) }

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