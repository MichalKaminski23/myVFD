package com.vfd.client.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vfd.client.data.remote.api.AuthApi
import com.vfd.client.data.remote.dtos.AuthResponseDto
import com.vfd.client.data.remote.dtos.PasswordDtos
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    json: Json,
    private val dataStore: DataStore<Preferences>,
) : BaseRepository(json) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    suspend fun register(userDto: UserDtos.UserCreate): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.register(userDto) }

    suspend fun login(userDto: UserDtos.UserLogin): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.login(userDto) }

    fun getToken(): Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    suspend fun saveToken(fullToken: String) {
        dataStore.edit { it[TOKEN_KEY] = fullToken }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    suspend fun changePassword(passwordDto: PasswordDtos.PasswordChange): ApiResult<Unit> =
        safeApiCall { authApi.changePassword(passwordDto) }
}