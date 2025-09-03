package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object OperationDtos {

    @Serializable
    data class OperationCreate(
        val firedepartmentId: Int,

        val operationType: String,

        val address: AddressDtos.AddressCreate,

        val operationDate: LocalDateTime,

        val description: String,

        val participantIds: List<Int> = emptyList()
    )

    @Serializable
    data class OperationPatch(
        val operationType: String? = null,

        //val address: AddressDtos.AddressPatch? = null,

        val operationDate: LocalDateTime? = null,

        val description: String? = null,

        val participantIds: List<Int>? = null
    )

    @Serializable
    data class OperationResponse(
        val operationId: Int,

        val firedepartment: FiredepartmentDtos.FiredepartmentResponse,

        val address: AddressDtos.AddressResponse,

        val operationType: OperationTypeDtos.OperationTypeResponse,

        val operationDate: LocalDateTime,

        val description: String,

        val participants: List<FirefighterDtos.FirefighterResponse>
    )
}