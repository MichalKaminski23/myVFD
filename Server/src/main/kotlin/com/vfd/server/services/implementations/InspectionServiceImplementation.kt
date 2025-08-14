package com.vfd.server.services.implementations

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.entities.Inspection
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.InspectionMapper
import com.vfd.server.repositories.AssetRepository
import com.vfd.server.repositories.InspectionRepository
import com.vfd.server.repositories.InspectionTypeRepository
import com.vfd.server.services.InspectionService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InspectionServiceImplementation(
    private val inspectionRepository: InspectionRepository,
    private val inspectionMapper: InspectionMapper,
    private val assetRepository: AssetRepository,
    private val inspectionTypeRepository: InspectionTypeRepository
) : InspectionService {

    private val INSPECTION_ALLOWED_SORTS = setOf(
        "inspectionId",
        "inspectionDate",
        "expirationDate",
        "asset.assetId",
        "inspectionType.inspectionType"
    )

    @Transactional
    override fun createInspection(insectionDto: InspectionDtos.InspectionCreate): InspectionDtos.InspectionResponse {

        val inspection: Inspection = inspectionMapper.toInspectionEntity(insectionDto)

        val asset = assetRepository.findById(insectionDto.assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", insectionDto.assetId) }
        inspection.asset = asset

        val inspectionType = inspectionTypeRepository.findById(insectionDto.inspectionType)
            .orElseThrow { ResourceNotFoundException("InspectionType", "code", insectionDto.inspectionType) }
        inspection.inspectionType = inspectionType

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
    }

    @Transactional(readOnly = true)
    override fun getAllInspections(page: Int, size: Int, sort: String): Page<InspectionDtos.InspectionResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INSPECTION_ALLOWED_SORTS,
            defaultSort = "inspectionId,asc",
            maxSize = 200
        )

        return inspectionRepository.findAll(pageable)
            .map(inspectionMapper::toInspectionDto)
    }

    @Transactional(readOnly = true)
    override fun getInspectionById(inspectionId: Int): InspectionDtos.InspectionResponse {

        val inspection = inspectionRepository.findById(inspectionId)
            .orElseThrow { ResourceNotFoundException("Inspection", "id", inspectionId) }

        return inspectionMapper.toInspectionDto(inspection)
    }

    @Transactional
    override fun updateInspection(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse {

        val inspection = inspectionRepository.findById(inspectionId)
            .orElseThrow { ResourceNotFoundException("Inspection", "id", inspectionId) }

        inspectionMapper.patchInspection(inspectionDto, inspection)

        inspectionDto.inspectionType
            ?.takeIf { it != inspection.inspectionType?.inspectionType }
            ?.let { code ->
                val inspectionType = inspectionTypeRepository.findById(code)
                    .orElseThrow { ResourceNotFoundException("InspectionType", "code", code) }
                inspection.inspectionType = inspectionType
            }

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
    }
}