package com.vfd.server.shared

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val messages: Map<String, String>,
    val path: String,
    val timestamp: String = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()
)