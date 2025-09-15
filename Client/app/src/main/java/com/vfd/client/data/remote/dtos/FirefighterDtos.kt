package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object FirefighterDtos {

    @Serializable
    data class FirefighterCreate(
        val userId: Int,

        val firedepartmentId: Int,

        //val role: Role
    )

    @Serializable
    data class FirefighterPatch(
        val role: Role? = null,

        val status: FirefighterStatus? = null
    )

    @Serializable
    data class FirefighterResponse(
        val firefighterId: Int,

        val user: UserDtos.UserResponse,

        val firedepartment: FiredepartmentDtos.FiredepartmentResponse,

        val role: Role,

        val status: FirefighterStatus
    )
}