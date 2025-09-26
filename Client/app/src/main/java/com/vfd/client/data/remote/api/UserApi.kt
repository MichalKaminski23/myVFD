package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.UserDtos
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {

    @PATCH("/api/users/me")
    suspend fun updateUser(
        @Body userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse

    @GET("/api/users/me")
    suspend fun getUserByEmailAddress(): UserDtos.UserResponse
}