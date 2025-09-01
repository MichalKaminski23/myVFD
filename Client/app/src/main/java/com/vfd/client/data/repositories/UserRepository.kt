package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.PageResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getAllUsers(
        page: Int = 0,
        size: Int = 20,
        sort: String = "createdAt,asc"
    ): PageResponse<UserDtos.UserResponse> =
        userApi.getAllUsers(page, size, sort)

    suspend fun getUserById(userId: Int): UserDtos.UserResponse =
        userApi.getUserById(userId)

    suspend fun updateUser(userId: Int, userPatchDto: UserDtos.UserPatch): UserDtos.UserResponse =
        userApi.updateUser(userId, userPatchDto)
}