package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("/api/users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "createdAt,asc"
    ): PageResponse<UserDtos.UserResponse>

    @GET("/api/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): UserDtos.UserResponse

    @PATCH("/api/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Int,
        @Body userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse

    @GET("/api/users/me")
    suspend fun getCurrentUser(): UserDtos.UserResponse
}