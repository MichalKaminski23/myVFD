package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.FirefighterActivityTypeApi
import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FirefighterActivityTypeRepository @Inject constructor(
    private val firefighterActivityTypeApi: FirefighterActivityTypeApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createFirefighterActivityType(
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate
    ): ApiResult<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> =
        safeApiCall {
            firefighterActivityTypeApi.createFirefighterActivityType(
                firefighterActivityTypeDto
            )
        }

    suspend fun getAllFirefighterActivityTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firefighterActivityType,asc"
    ): ApiResult<PageResponse<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse>> =
        safeApiCall { firefighterActivityTypeApi.getAllFirefighterActivityTypes(page, size, sort) }

    suspend fun getFirefighterActivityTypeByCode(
        firefighterActivityTypeCode: String
    ): ApiResult<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> =
        safeApiCall {
            firefighterActivityTypeApi.getFirefighterActivityTypeByCode(
                firefighterActivityTypeCode
            )
        }

    suspend fun updateFirefighterActivityType(
        firefighterActivityTypeCode: String,
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch
    ): ApiResult<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> =
        safeApiCall {
            firefighterActivityTypeApi.updateFirefighterActivityType(
                firefighterActivityTypeCode,
                firefighterActivityTypeDto
            )
        }
}