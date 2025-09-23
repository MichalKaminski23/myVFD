package com.vfd.server.services

import com.vfd.server.dtos.UserDtos
import com.vfd.server.shared.PageResponse

interface UserService {

    fun updateUser(
        emailAddress: String,
        userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse
    
    fun getUserByEmailAddress(emailAddress: String): UserDtos.UserResponse

    fun getAllUsersDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "userId,asc"
    ): PageResponse<UserDtos.UserResponse>

    fun getUserByIdDev(userId: Int): UserDtos.UserResponse


    fun updateUserDev(userId: Int, userDto: UserDtos.UserPatch): UserDtos.UserResponse
}