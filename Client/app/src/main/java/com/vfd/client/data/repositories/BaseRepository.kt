package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.R
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.ErrorResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class BaseRepository(
    private val json: Json,
    protected open val context: Context
) {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
        return try {
            ApiResult.Success(apiCall())

        } catch (exception: HttpException) {
            val errorBody = exception.response()?.errorBody()?.string()

            var fieldErrors: Map<String, String> = emptyMap()
            var message = context.getString(R.string.server_error)
            val code: Int = exception.code()

            if (!errorBody.isNullOrBlank()) {
                try {
                    val jsonElement = json.parseToJsonElement(errorBody).jsonObject

                    if ("messages" in jsonElement) {
                        fieldErrors = jsonElement["messages"]!!.jsonObject.mapValues {
                            it.value.jsonPrimitive.content
                        }
                        message = context.getString(R.string.validation_error)
                    } else {
                        val errorResponse =
                            json.decodeFromString(ErrorResponse.serializer(), errorBody)
                        message = errorResponse.message
                    }
                } catch (_: Exception) {
                    message = context.getString(R.string.server_error)
                }
            }

            ApiResult.Error(message, code, fieldErrors)

        } catch (exception: SocketTimeoutException) {
            val code = (exception.cause as? HttpException)?.code()
            ApiResult.Error(context.getString(R.string.connection_timed_out), code)

        } catch (exception: IOException) {
            val code = (exception.cause as? HttpException)?.code()
            ApiResult.Error(context.getString(R.string.network_error), code)

        } catch (exception: Exception) {
            val code = (exception.cause as? HttpException)?.code()
            ApiResult.Error(
                "${context.getString(R.string.unexpected_error)}: ${exception.message}",
                code
            )
        }
    }
}