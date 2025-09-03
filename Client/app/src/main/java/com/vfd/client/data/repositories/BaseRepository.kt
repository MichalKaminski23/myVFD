package com.vfd.client.data.repositories

import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(apiCall())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            println("ERROR BODY: $errorBody") // 👈 sprawdź co backend zwraca
            val errorResponse = try {
                errorBody?.let { Json.decodeFromString(ErrorResponse.serializer(), it) }
            } catch (_: Exception) {
                null
            }
            ApiResult.Error(errorResponse?.message ?: "Server error", e)
        } catch (e: Exception) {
            ApiResult.Error("Unexpected error: ${e.message}", e)
        }
    }
}
