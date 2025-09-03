package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.AuthApi
import com.vfd.client.data.remote.dtos.AuthResponseDto
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    json: Json
) : BaseRepository(json) {

    suspend fun register(userDto: UserDtos.UserCreate): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.register(userDto) }
    
    suspend fun login(userDto: UserDtos.UserLogin): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.login(userDto) }

    suspend fun logout(): ApiResult<Unit> =
        safeApiCall { authApi.logout() }
}