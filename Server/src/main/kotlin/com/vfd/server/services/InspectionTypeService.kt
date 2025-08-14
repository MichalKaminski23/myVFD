package com.vfd.server.services

import com.vfd.server.dtos.InspectionTypeDtos
import org.springframework.data.domain.Page

interface InspectionTypeService {

    fun createInspectionType(inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate): InspectionTypeDtos.InspectionTypeResponse

    fun getAllInspectionTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionType,asc"
    ): Page<InspectionTypeDtos.InspectionTypeResponse>

    fun getInspectionTypeByCode(inspectionTypeCode: String): InspectionTypeDtos.InspectionTypeResponse

    fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse
}