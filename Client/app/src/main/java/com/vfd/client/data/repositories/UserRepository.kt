package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.UserApi
import com.vfd.client.data.remote.dtos.UserDtos
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi
) {
    suspend fun getAllUsers(): List<UserDtos.UserResponse> =
        userApi.getAllUsers().items

    suspend fun getUserById(userId: Int): UserDtos.UserResponse =
        userApi.getUserById(userId)

    suspend fun updateUser(userId: Int, userPatchDto: UserDtos.UserPatch): UserDtos.UserResponse =
        userApi.updateUser(userId, userPatchDto)
}