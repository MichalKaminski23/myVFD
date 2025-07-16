package com.vfd.server.dtos

import java.time.LocalDateTime

data class UserDto(
    val userId: Int?,
    val firstName: String,
    val lastName: String,
    val address: AddressDto,
    val phoneNumber: String,
    val emailAddress: String,
    val createdAt: LocalDateTime,
    val loggedAt: LocalDateTime?,
    val isActive: Boolean
)