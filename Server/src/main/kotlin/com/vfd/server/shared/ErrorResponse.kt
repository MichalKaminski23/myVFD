package com.vfd.server.shared

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: String = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()
)