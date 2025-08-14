package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.Firefighter
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.FirefighterMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.FirefighterService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterServiceImplementation(
    private val firefighterRepository: FirefighterRepository,
    private val firefighterMapper: FirefighterMapper,
    private val userRepository: UserRepository,
    private val firedepartmentRepository: FiredepartmentRepository
) : FirefighterService {

    private val FIREFIGHTER_ALLOWED_SORTS = setOf(
        "firefighterId",
        "role",
        "user.userId",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createFirefighter(
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {

        val user = userRepository.findById(firefighterDto.userId)
            .orElseThrow { ResourceNotFoundException("User", "id", firefighterDto.userId) }

        if (firefighterRepository.existsById(firefighterDto.userId)) {
            throw ResourceConflictException("Firefighter", "id", firefighterDto.userId)
        }

        val firedepartment = firedepartmentRepository.findById(firefighterDto.firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", firefighterDto.firedepartmentId) }

        val firefighter: Firefighter = firefighterMapper.toFirefighterEntity(firefighterDto)

        firefighter.user = user
        firefighter.firedepartment = firedepartment

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllFirefighters(
        page: Int,
        size: Int,
        sort: String
    ): Page<FirefighterDtos.FirefighterResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREFIGHTER_ALLOWED_SORTS,
            defaultSort = "firefighterId,asc",
            maxSize = 200
        )

        return firefighterRepository.findAll(pageable)
            .map(firefighterMapper::toFirefighterDto)
    }

    @Transactional(readOnly = true)
    override fun getFirefighterById(
        firefighterId: Int
    ): FirefighterDtos.FirefighterResponse {

        val firefighter = firefighterRepository.findById(firefighterId)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", firefighterId) }

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    @Transactional
    override fun updateFirefighter(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse {

        val firefighter = firefighterRepository.findById(firefighterId)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", firefighterId) }

        firefighterMapper.patchFirefighter(firefighterDto, firefighter)

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }
}