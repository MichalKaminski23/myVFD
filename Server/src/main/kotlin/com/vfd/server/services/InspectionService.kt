package com.vfd.server.services

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.shared.PageResponse

interface InspectionService {

    fun createInspection(insectionDto: InspectionDtos.InspectionCreate): InspectionDtos.InspectionResponse

    fun getAllInspections(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionId,asc"
    ): PageResponse<InspectionDtos.InspectionResponse>

    fun getInspectionById(inspectionId: Int): InspectionDtos.InspectionResponse

    fun updateInspection(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse
}