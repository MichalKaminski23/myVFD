package com.vfd.server.services.implementations

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.mappers.InspectionMapper
import com.vfd.server.repositories.AssetRepository
import com.vfd.server.repositories.InspectionRepository
import com.vfd.server.repositories.InspectionTypeRepository
import com.vfd.server.services.InspectionService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.findByIdOrThrow
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InspectionServiceImplementation(
    private val inspectionRepository: InspectionRepository,
    private val inspectionMapper: InspectionMapper,
    private val assetRepository: AssetRepository,
    private val inspectionTypeRepository: InspectionTypeRepository
) : InspectionService {

    override fun createInspection(
        emailAddress: String,
        inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse {
        TODO("Not yet implemented")
    }

    override fun getInspections(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<InspectionDtos.InspectionResponse> {
        TODO("Not yet implemented")
    }

    override fun updateInspection(
        emailAddress: String,
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse {
        TODO("Not yet implemented")
    }

    private val INSPECTION_ALLOWED_SORTS = setOf(
        "inspectionId",
        "inspectionDate",
        "expirationDate",
        "asset.assetId",
        "inspectionType.inspectionType"
    )

    @Transactional
    override fun createInspectionDev(inspectionDto: InspectionDtos.InspectionCreate): InspectionDtos.InspectionResponse {

        val inspection = inspectionMapper.toInspectionEntity(inspectionDto)

        val asset = assetRepository.findByIdOrThrow(inspectionDto.assetId)
        inspection.asset = asset

        val inspectionType = inspectionTypeRepository.findByIdOrThrow(inspectionDto.inspectionType)
        inspection.inspectionType = inspectionType

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
    }

    @Transactional(readOnly = true)
    override fun getAllInspectionsDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<InspectionDtos.InspectionResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INSPECTION_ALLOWED_SORTS,
            defaultSort = "inspectionId,asc",
            maxSize = 200
        )

        return inspectionRepository.findAll(pageable)
            .map(inspectionMapper::toInspectionDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getInspectionByIdDev(inspectionId: Int): InspectionDtos.InspectionResponse {

        val inspection = inspectionRepository.findByIdOrThrow(inspectionId)

        return inspectionMapper.toInspectionDto(inspection)
    }

    @Transactional
    override fun updateInspectionDev(
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse {

        val inspection = inspectionRepository.findByIdOrThrow(inspectionId)

        inspectionMapper.patchInspection(inspectionDto, inspection)

        inspectionDto.inspectionType
            ?.takeIf { it != inspection.inspectionType?.inspectionType }
            ?.let { code ->
                val inspectionType = inspectionTypeRepository.findByIdOrThrow(code)
                inspection.inspectionType = inspectionType
            }

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
    }
}