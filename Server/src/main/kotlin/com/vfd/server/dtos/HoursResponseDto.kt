package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO representing the total hours for a specific year and quarter")
data class HoursResponseDto(
    @field:Schema(description = "Year for which the hours are reported", example = "2023")
    val year: Int,

    @field:Schema(description = "Quarter of the year (1-4)", example = "2")
    val quarter: Int,

    @field:Schema(description = "Total hours recorded for the specified year and quarter", example = "42")
    val hours: Double
)