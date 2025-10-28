package com.vfd.server.services

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.PasswordDtos
import com.vfd.server.dtos.UserDtos

interface AuthService {

    fun register(userDto: UserDtos.UserCreate): AuthResponseDto

    fun login(userDto: UserDtos.UserLogin): AuthResponseDto

    fun generateJwt(emailAddress: String, password: String): String

    fun changePassword(emailAddress: String, passwordDto: PasswordDtos.PasswordChange)
}