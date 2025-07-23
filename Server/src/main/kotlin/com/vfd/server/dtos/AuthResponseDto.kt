package com.vfd.server.dtos

data class AuthResponseDto(
    val token: String,
    val tokenType: String = "Bearer"
)
