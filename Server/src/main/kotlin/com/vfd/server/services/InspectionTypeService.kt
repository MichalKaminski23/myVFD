package com.vfd.server.services

import com.vfd.server.dtos.InspectionTypeDtos
import com.vfd.server.shared.PageResponse

interface InspectionTypeService {

    fun createInspectionType(inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate): InspectionTypeDtos.InspectionTypeResponse

    fun getAllInspectionTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "inspectionType,asc"
    ): PageResponse<InspectionTypeDtos.InspectionTypeResponse>

    fun getInspectionTypeByCode(inspectionTypeCode: String): InspectionTypeDtos.InspectionTypeResponse

    fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse
}