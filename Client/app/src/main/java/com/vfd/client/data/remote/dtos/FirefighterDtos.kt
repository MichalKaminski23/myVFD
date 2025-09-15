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

        val firstName: String,

        val lastName: String,

        //val user: UserDtos.UserResponse,
        val userId: Int,

        // val firedepartment: FiredepartmentDtos.FiredepartmentResponse,
        val firedepartmentId: Int,

        val firedepartmentName: String,

        val role: Role,

        val status: FirefighterStatus
    )
}