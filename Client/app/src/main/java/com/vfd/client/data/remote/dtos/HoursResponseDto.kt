package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

@Serializable
data class HoursResponseDto(
    val year: Int,

    val quarter: Int,

    val hours: Double
)