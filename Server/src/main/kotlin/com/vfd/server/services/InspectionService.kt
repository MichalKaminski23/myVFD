package com.vfd.server.services

import com.vfd.server.dtos.InspectionDtos
import org.springframework.data.domain.Page

interface InspectionService {

    fun createInspection(insectionDto: InspectionDtos.InspectionCreate): InspectionDtos.InspectionResponse

    fun getAllInspections(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionId,asc"
    ): Page<InspectionDtos.InspectionResponse>

    fun getInspectionById(inspectionId: Int): InspectionDtos.InspectionResponse

    fun updateInspection(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse
}