package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.AuthResponseDto
import com.vfd.client.data.remote.dtos.UserDtos
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/register")
    suspend fun register(@Body userDto: UserDtos.UserCreate): AuthResponseDto

    @POST("/api/auth/login")
    suspend fun login(@Body userDto: UserDtos.UserLogin): AuthResponseDto
}