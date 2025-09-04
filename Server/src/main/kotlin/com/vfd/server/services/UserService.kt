package com.vfd.server.services

import com.vfd.server.dtos.UserDtos
import com.vfd.server.shared.PageResponse

interface UserService {

    fun getAllUsers(
        page: Int = 0,
        size: Int = 20,
        sort: String = "userId,asc"
    ): PageResponse<UserDtos.UserResponse>

    fun getUserById(userId: Int): UserDtos.UserResponse

    fun getUserByEmailAddress(emailAddress: String): UserDtos.UserResponse

    fun updateUser(userId: Int, userDto: UserDtos.UserPatch): UserDtos.UserResponse
}