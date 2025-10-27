package com.vfd.server.exceptions

open class LocalizedException(
    val messageKey: String,
    val args: Array<Any>? = null,
    defaultMessage: String? = null
) : RuntimeException(defaultMessage ?: messageKey)