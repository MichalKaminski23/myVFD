package com.vfd.client.utils

sealed class ApiResult<out T>(
    val data: T? = null,
    val message: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
) {
    class Loading<T> : ApiResult<T>(null)

    class Success<T>(data: T) : ApiResult<T>(data)

    class Error(
        message: String,
        val cause: Throwable? = null,
        fieldErrors: Map<String, String> = emptyMap()
    ) : ApiResult<Nothing>(null, message, fieldErrors)
}
