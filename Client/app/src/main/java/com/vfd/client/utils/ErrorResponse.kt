package com.vfd.client.utils

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,

    val error: String,

    val message: String,

    val path: String,

    val timestamp: String
)