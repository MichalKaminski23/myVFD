package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) : BaseRepository() {

    suspend fun getAllUsers(
        page: Int = 0,
        size: Int = 20,
        sort: String = "createdAt,asc"
    ): ApiResult<PageResponse<UserDtos.UserResponse>> =
        safeApiCall { userApi.getAllUsers(page, size, sort) }

    suspend fun getUserById(userId: Int): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.getUserById(userId) }

    suspend fun updateUser(
        userId: Int,
        dto: UserDtos.UserPatch
    ): ApiResult<UserDtos.UserResponse> =
        safeApiCall { userApi.updateUser(userId, dto) }
}