package com.vfd.client

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)

data class UserTestDto(
    val userId: Int,

    val firstName: String,

    val lastName: String,

    val address: AddressTestDto,

    val emailAddress: String,

    val phoneNumber: String,

    val createdAt: LocalDateTime,

    val loggedAt: LocalDateTime,

    val active: Boolean
)