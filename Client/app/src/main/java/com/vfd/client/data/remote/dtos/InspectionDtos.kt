package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object InspectionDtos {

    @Serializable
    data class InspectionCreate(
        val assetId: Int,

        val inspectionType: String,

        val inspectionDate: LocalDateTime,

        val expirationDate: LocalDateTime? = null
    )

    @Serializable
    data class InspectionPatch(
        val inspectionType: String? = null,

        val inspectionDate: LocalDateTime? = null,

        val expirationDate: LocalDateTime? = null
    )

    @Serializable
    data class InspectionResponse(
        val inspectionId: Int,

        val assetId: Int,

        val inspectionTypeName: String,

        val inspectionDate: LocalDateTime,

        val expirationDate: LocalDateTime?
    )
}