package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO used as a response after successful authentication")
data class AuthResponseDto(

    @field:Schema(
        description = "JWT token used for authenticating requests",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
    )
    val token: String,

    @field:Schema(description = "Token type used in Authorization header", example = "Bearer")
    val tokenType: String = "Bearer"
)
