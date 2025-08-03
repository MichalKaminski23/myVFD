package com.vfd.server.entities

data class ErrorResponse(
    val status: Int,
    val message: String,
    val path: String
)