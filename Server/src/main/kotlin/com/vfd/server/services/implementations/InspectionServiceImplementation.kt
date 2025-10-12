package com.vfd.server.services.implementations

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.mappers.InspectionMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.InspectionService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InspectionServiceImplementation(
    private val inspectionRepository: InspectionRepository,
    private val inspectionMapper: InspectionMapper,
    private val assetRepository: AssetRepository,
    private val inspectionTypeRepository: InspectionTypeRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository
) : InspectionService {

    @Transactional
    override fun createInspection(
        emailAddress: String,
        inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val asset = assetRepository.findByIdOrThrow(inspectionDto.assetId)

        val inspectionType = inspectionTypeRepository.findByIdOrThrow(inspectionDto.inspectionType)

        validateDates(inspectionDto.inspectionDate, inspectionDto.expirationDate, "Inspection")

        val inspection = inspectionMapper.toInspectionEntity(inspectionDto).apply {
            this.asset = asset
            this.inspectionType = inspectionType
        }

        inspection.requireSameFiredepartment(firedepartmentId)

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
    }

    @Transactional(readOnly = true)
    override fun getInspections(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<InspectionDtos.InspectionResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INSPECTION_ALLOWED_SORTS,
            defaultSort = "expirationDate,asc",
            maxSize = 200
        )

        return inspectionRepository.findAllByAssetFiredepartmentFiredepartmentId(firedepartmentId, pageable)
            .map(inspectionMapper::toInspectionDto)
            .toPageResponse()
    }

    @Transactional
    override fun updateInspection(
        emailAddress: String,
        inspectionId: Int,
        inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val inspection = inspectionRepository.findByIdOrThrow(inspectionId)

        inspection.requireSameFiredepartment(firedepartmentId)

        val effectiveInspectionDate = inspectionDto.inspectionDate ?: inspection.inspectionDate
        val effectiveExpirationDate = inspectionDto.expirationDate ?: inspection.expirationDate

        validateDates(effectiveInspectionDate, effectiveExpirationDate, "Inspection")

        inspectionMapper.patchInspection(inspectionDto, inspection)

        inspectionDto.inspectionType
            ?.takeIf { it != inspection.inspectionType?.inspectionType }
            ?.let { code ->
                val inspectionType = inspectionTypeRepository.findByIdOrThrow(code)
                inspection.inspectionType = inspectionType
            }

        return inspectionMapper.toInspectionDto(inspectionRepository.save(inspection))
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

        validateDates(inspectionDto.inspectionDate, inspectionDto.expirationDate, "Inspection")

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

        val effectiveInspectionDate = inspectionDto.inspectionDate ?: inspection.inspectionDate
        val effectiveExpirationDate = inspectionDto.expirationDate ?: inspection.expirationDate

        validateDates(effectiveInspectionDate, effectiveExpirationDate, "Inspection")

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