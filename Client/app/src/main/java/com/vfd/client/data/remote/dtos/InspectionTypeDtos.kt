package com.vfd.client.data.remote.dtos

import kotlinx.serialization.Serializable

object InspectionTypeDtos {

    @Serializable
    data class InspectionTypeCreate(
        val inspectionType: String,

        val name: String
    )

    @Serializable
    data class InspectionTypePatch(
        val name: String? = null
    )

    @Serializable
    data class InspectionTypeResponse(
        val inspectionType: String,

        val name: String
    )
}