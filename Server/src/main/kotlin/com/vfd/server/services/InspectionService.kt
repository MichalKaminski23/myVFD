package com.vfd.server.services

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.shared.PageResponse

interface InspectionService {

    fun createInspection(
        emailAddress: String,
        inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse

    fun getInspections(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionDate,desc",
        emailAddress: String
    ): PageResponse<InspectionDtos.InspectionResponse>

    fun updateInspection(
        emailAddress: String,
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse

    fun createInspectionDev(inspectionDto: InspectionDtos.InspectionCreate): InspectionDtos.InspectionResponse

    fun getAllInspectionsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionId,asc"
    ): PageResponse<InspectionDtos.InspectionResponse>

    fun getInspectionByIdDev(inspectionId: Int): InspectionDtos.InspectionResponse

    fun updateInspectionDev(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse
}