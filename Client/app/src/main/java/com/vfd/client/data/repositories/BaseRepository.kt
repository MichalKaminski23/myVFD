package com.vfd.client.data.repositories

import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.ErrorResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRepository(
    private val json: Json
) {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(apiCall())

        } catch (exception: HttpException) {
            val errorBody = exception.response()?.errorBody()?.string()

            var fieldErrors: Map<String, String> = emptyMap()
            var message: String = "Server error"

            if (!errorBody.isNullOrBlank()) {
                try {
                    val jsonElement = json.parseToJsonElement(errorBody).jsonObject

                    if ("messages" in jsonElement) {
                        fieldErrors = jsonElement["messages"]!!.jsonObject.mapValues {
                            it.value.jsonPrimitive.content
                        }
                        message = "Validation error"
                    } else {
                        // klasyczny ErrorResponse
                        val errorResponse =
                            json.decodeFromString(ErrorResponse.serializer(), errorBody)
                        message = errorResponse.message ?: "Server error"
                    }
                } catch (e: Exception) {
                    message = "Server error"
                }
            }

            ApiResult.Error(message, exception, fieldErrors)

        } catch (exception: SocketTimeoutException) {
            ApiResult.Error("Connection timed out. Please try again.", exception)

        } catch (exception: IOException) {
            ApiResult.Error("Network error: could not connect to server.", exception)

        } catch (exception: Exception) {
            ApiResult.Error("Unexpected error: ${exception.message}", exception)
        }
    }
}
