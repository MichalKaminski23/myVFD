package com.vfd.client.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vfd.client.data.remote.api.AuthApi
import com.vfd.client.data.remote.dtos.AuthResponseDto
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    json: Json,
    private val dataStore: DataStore<Preferences>,
) : BaseRepository(json) {

    suspend fun register(userDto: UserDtos.UserCreate): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.register(userDto) }

    suspend fun login(userDto: UserDtos.UserLogin): ApiResult<AuthResponseDto> =
        safeApiCall { authApi.login(userDto) }

    suspend fun logout(): ApiResult<Unit> =
        safeApiCall { authApi.logout() }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    fun getToken(): Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }

    suspend fun saveToken(fullToken: String) {
        dataStore.edit { it[TOKEN_KEY] = fullToken }
    }

    fun printToken() {
        runBlocking {
            val token = dataStore.data.firstOrNull()?.get(TOKEN_KEY)
            Log.d("TokenProvider", "Current token: $token")
        }
    }

    suspend fun clearToken() {
        printToken()
        dataStore.edit { it.remove(TOKEN_KEY) }
        printToken()
    }
}