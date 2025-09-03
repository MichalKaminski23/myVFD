package com.vfd.client.utils

sealed class ApiResult<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : ApiResult<T>(null)

    class Success<T>(data: T) : ApiResult<T>(data)

    class Error(message: String, val cause: Throwable? = null) : ApiResult<Nothing>(null, message)
}