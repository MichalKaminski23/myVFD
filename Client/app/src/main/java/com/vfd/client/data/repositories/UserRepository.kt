package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun updateUser(
        userDto: UserDtos.UserPatch
    ): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.updateUser(userDto) }

    suspend fun getUserByEmailAddress(): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.getUserByEmailAddress() }
}