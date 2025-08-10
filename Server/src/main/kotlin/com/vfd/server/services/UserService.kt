package com.vfd.server.services

import com.vfd.server.dtos.UserDtos
import org.springframework.data.domain.Page

interface UserService {

    fun createUser(userDto: UserDtos.UserCreate): UserDtos.UserResponse
    fun getAllUsers(
        page: Int = 0,
        size: Int = 20,
        sort: String = "createdAt,desc"
    ): Page<UserDtos.UserResponse>

    fun getUserById(userId: Int): UserDtos.UserResponse
    fun updateUser(userId: Int, userDto: UserDtos.UserPatch): UserDtos.UserResponse
}