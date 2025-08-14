package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterActivityTypeDtos
import com.vfd.server.entities.FirefighterActivityType
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.FirefighterActivityTypeMapper
import com.vfd.server.repositories.FirefighterActivityTypeRepository
import com.vfd.server.services.FirefighterActivityTypeService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterActivityTypeServiceImplementation(
    private val firefighterActivityTypeRepository: FirefighterActivityTypeRepository,
    private val firefighterActivityTypeMapper: FirefighterActivityTypeMapper
) : FirefighterActivityTypeService {

    private val FIREFIGHTER_ACTIVITY_TYPES__ALLOWED_SORTS = setOf("firefighterActivityType", "name")

    @Transactional
    override fun createFirefighterActivityType(
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse {

        val firefighterActivityType: FirefighterActivityType =
            firefighterActivityTypeMapper.toFirefighterActivityTypeEntity(firefighterActivityTypeDto)

        return firefighterActivityTypeMapper.toFirefighterActivityTypeDto(
            firefighterActivityTypeRepository.save(firefighterActivityType)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllFirefighterActivityTypes(
        page: Int,
        size: Int,
        sort: String
    ): Page<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREFIGHTER_ACTIVITY_TYPES__ALLOWED_SORTS,
            defaultSort = "firefighterActivityType,asc",
            maxSize = 200
        )

        return firefighterActivityTypeRepository.findAll(pageable)
            .map(firefighterActivityTypeMapper::toFirefighterActivityTypeDto)
    }

    @Transactional(readOnly = true)
    override fun getFirefighterActivityTypeByCode(
        firefighterActivityTypeCode: String
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse {

        val firefighterActivityType = firefighterActivityTypeRepository.findById(firefighterActivityTypeCode)
            .orElseThrow { ResourceNotFoundException("FirefighterActivityType", "code", firefighterActivityTypeCode) }

        return firefighterActivityTypeMapper.toFirefighterActivityTypeDto(firefighterActivityType)
    }

    @Transactional
    override fun updateFirefighterActivityType(
        firefighterActivityTypeCode: String,
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse {

        val firefighterActivityType = firefighterActivityTypeRepository.findById(firefighterActivityTypeCode)
            .orElseThrow { ResourceNotFoundException("FirefighterActivityType", "code", firefighterActivityTypeCode) }

        firefighterActivityTypeMapper.patchFirefighterActivityType(
            firefighterActivityTypeDto,
            firefighterActivityType
        )

        return firefighterActivityTypeMapper.toFirefighterActivityTypeDto(
            firefighterActivityTypeRepository.save(firefighterActivityType)
        )
    }
}