package com.vfd.client.data.repositories

import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException

abstract class BaseRepository(
    private val json: Json
) {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(apiCall())

        } catch (exception: HttpException) {
            val errorBody = exception.response()?.errorBody()?.string()

            val errorResponse = try {
                errorBody?.let { json.decodeFromString(ErrorResponse.serializer(), it) }

            } catch (_: Exception) {
                null
            }

            ApiResult.Error(errorResponse?.message ?: "Server error", exception)

//        } catch (exception: IOException) {
//            ApiResult.Error("Network error: check your connection", exception)
//
        } catch (exception: Exception) {
            ApiResult.Error("Unexpected error: ${exception.message}", exception)
        }
    }
}
