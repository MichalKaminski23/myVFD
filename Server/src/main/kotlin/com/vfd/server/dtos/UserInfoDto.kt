package com.vfd.server.dtos

import java.time.LocalDateTime

data class UserInfoDto(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val phoneNumber: String,
    val addressId: Int,
    val createdAt: LocalDateTime,
    val loggedAt: LocalDateTime,
    val isActive: Boolean
)