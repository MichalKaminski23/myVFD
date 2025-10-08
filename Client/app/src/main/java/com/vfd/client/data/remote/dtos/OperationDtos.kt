package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object OperationDtos {

    @Serializable
    data class OperationCreate(
        val operationType: String,

        val address: AddressDtos.AddressCreate,

        val operationDate: LocalDateTime,

        val description: String,

        val participantsIds: Set<Int>? = null
    )

    @Serializable
    data class OperationPatch(
        val operationType: String? = null,

        val address: AddressDtos.AddressCreate? = null,

        val operationDate: LocalDateTime? = null,

        val description: String? = null,

        val participantsIds: Set<Int>? = null
    )

    @Serializable
    data class OperationResponse(
        val operationId: Int,

        val firedepartmentId: Int,

        val address: AddressDtos.AddressResponse,

        val operationTypeName: String,

        val operationDate: LocalDateTime,

        val description: String,

        val participants: List<FirefighterDtos.FirefighterResponseShort>
    )
}