package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object PasswordDtos {

    @Serializable
    data class PasswordChange(

        val currentPassword: String,

        val newPassword: String
    )
}