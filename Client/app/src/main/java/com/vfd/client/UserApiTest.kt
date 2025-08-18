package com.vfd.client

import retrofit2.http.GET

interface UserApiTest {

    @GET("/api/users")
    suspend fun getUsers(): PageResponse<UserTestDto>
}