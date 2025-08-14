package com.vfd.server.services.implementations

import com.vfd.server.dtos.InspectionTypeDtos
import com.vfd.server.entities.InspectionType
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.InspectionTypeMapper
import com.vfd.server.repositories.InspectionTypeRepository
import com.vfd.server.services.InspectionTypeService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InspectionTypeServiceImplementation(
    private val inspectionTypeRepository: InspectionTypeRepository,
    private val inspectionTypeMapper: InspectionTypeMapper
) : InspectionTypeService {

    private val INSPECTION_TYPE_ALLOWED_SORTS = setOf("inspectionType", "name")

    @Transactional
    override fun createInspectionType(
        inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate
    ): InspectionTypeDtos.InspectionTypeResponse {

        val inspectionType: InspectionType =
            inspectionTypeMapper.toInspectionTypeEntity(inspectionTypeDto)

        return inspectionTypeMapper.toInspectionTypeDto(
            inspectionTypeRepository.save(inspectionType)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllInspectionTypes(
        page: Int,
        size: Int,
        sort: String
    ): Page<InspectionTypeDtos.InspectionTypeResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INSPECTION_TYPE_ALLOWED_SORTS,
            defaultSort = "inspectionType,asc",
            maxSize = 200
        )

        return inspectionTypeRepository.findAll(pageable)
            .map(inspectionTypeMapper::toInspectionTypeDto)
    }

    @Transactional(readOnly = true)
    override fun getInspectionTypeByCode(
        inspectionTypeCode: String
    ): InspectionTypeDtos.InspectionTypeResponse {

        val inspectionType = inspectionTypeRepository.findById(inspectionTypeCode)
            .orElseThrow { ResourceNotFoundException("InspectionType", "code", inspectionTypeCode) }

        return inspectionTypeMapper.toInspectionTypeDto(inspectionType)
    }

    @Transactional
    override fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse {
        val entity = inspectionTypeRepository.findById(inspectionTypeCode)
            .orElseThrow { ResourceNotFoundException("InspectionType", "code", inspectionTypeCode) }

        inspectionTypeMapper.patchInspectionType(inspectionTypeDto, entity)

        return inspectionTypeMapper.toInspectionTypeDto(
            inspectionTypeRepository.save(entity)
        )
    }
}