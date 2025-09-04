package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(

    val token: String,

    val tokenType: String = "Bearer"
)