package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    json: Json
) : BaseRepository(json) {

    suspend fun updateUser(
        userDto: UserDtos.UserPatch
    ): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.updateUser(userDto) }

    suspend fun getUserByEmailAddress(): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.getUserByEmailAddress() }
}