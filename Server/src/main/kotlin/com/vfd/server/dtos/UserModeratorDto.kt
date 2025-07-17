package com.vfd.server.dtos

data class UserModeratorDto(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val isActive: Boolean
)