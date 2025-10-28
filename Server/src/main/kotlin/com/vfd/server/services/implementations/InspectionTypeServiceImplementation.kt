package com.vfd.server.services.implementations

import com.vfd.server.dtos.InspectionTypeDtos
import com.vfd.server.mappers.InspectionTypeMapper
import com.vfd.server.repositories.InspectionTypeRepository
import com.vfd.server.services.InspectionTypeService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InspectionTypeServiceImplementation(
    private val inspectionTypeRepository: InspectionTypeRepository,
    private val inspectionTypeMapper: InspectionTypeMapper
) : InspectionTypeService {

    private val sorts = setOf("inspectionType", "name")

    @Transactional
    override fun createInspectionType(
        inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate
    ): InspectionTypeDtos.InspectionTypeResponse {

        val inspectionType = inspectionTypeMapper.toInspectionTypeEntity(inspectionTypeDto)

        inspectionTypeRepository.assertNotExistsByInspectionType(inspectionTypeDto.inspectionType.uppercase())

        return inspectionTypeMapper.toInspectionTypeDto(
            inspectionTypeRepository.save(inspectionType)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllInspectionTypes(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<InspectionTypeDtos.InspectionTypeResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "inspectionType,asc",
            maxSize = 200
        )

        return inspectionTypeRepository.findAll(pageable)
            .map(inspectionTypeMapper::toInspectionTypeDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getInspectionTypeByCode(
        inspectionTypeCode: String
    ): InspectionTypeDtos.InspectionTypeResponse {

        val inspectionType = inspectionTypeRepository.findByIdOrThrow(inspectionTypeCode)

        return inspectionTypeMapper.toInspectionTypeDto(inspectionType)
    }

    @Transactional
    override fun updateInspectionType(
        inspectionTypeCode: String,
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse {

        val inspectionType = inspectionTypeRepository.findByIdOrThrow(inspectionTypeCode)

        inspectionTypeMapper.patchInspectionType(inspectionTypeDto, inspectionType)

        return inspectionTypeMapper.toInspectionTypeDto(
            inspectionTypeRepository.save(inspectionType)
        )
    }
}