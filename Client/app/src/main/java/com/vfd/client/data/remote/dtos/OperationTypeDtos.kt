package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object OperationTypeDtos {

    @Serializable
    data class OperationTypeCreate(
        val operationType: String,

        val name: String
    )

    @Serializable
    data class OperationTypePatch(
        val name: String? = null
    )

    @Serializable
    data class OperationTypeResponse(
        val operationType: String,

        val name: String
    )
}