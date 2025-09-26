package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.FirefighterRole
import com.vfd.server.entities.FirefighterStatus
import com.vfd.server.exceptions.InvalidStatusException
import com.vfd.server.mappers.FirefighterMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.FirefighterService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterServiceImplementation(
    private val firefighterRepository: FirefighterRepository,
    private val firefighterMapper: FirefighterMapper,
    private val userRepository: UserRepository,
    private val firedepartmentRepository: FiredepartmentRepository
) : FirefighterService {

    fun validateStatus(status: String?) {
        try {
            FirefighterStatus.valueOf(status!!)
        } catch (exception: IllegalArgumentException) {
            throw InvalidStatusException(
                "Invalid status: ${status!!}. Allowed: ${
                    FirefighterStatus.entries.joinToString()
                }"
            )
        }
    }

    fun validateRole(role: String?) {
        try {
            FirefighterRole.valueOf(role!!)
        } catch (exception: IllegalArgumentException) {
            throw InvalidStatusException(
                "Invalid role: ${role!!}. Allowed: ${
                    FirefighterRole.entries.joinToString()
                }"
            )
        }
    }

    @Transactional
    override fun createFirefighter(
        emailAddress: String,
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {

        val userModerator = userRepository.findByEmailOrThrow(emailAddress)

        firefighterRepository.findByIdOrThrow(userModerator.userId!!)


        val userCreated = userRepository.findByIdOrThrow(firefighterDto.userId)

        firefighterRepository.assertNotExistsByUserId(userCreated.userId!!)


        val firedepartment = firedepartmentRepository.findByIdOrThrow(firefighterDto.firedepartmentId)

        val firefighter = firefighterMapper.toFirefighterEntity(firefighterDto)

        firefighter.user = userCreated
        firefighter.firedepartment = firedepartment
        firefighter.role = FirefighterRole.USER
        firefighter.status = FirefighterStatus.PENDING

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }

    @Transactional(readOnly = true)
    override fun getFirefighters(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            FIREFIGHTER_ALLOWED_SORTS,
            "user.firstName,asc",
            200
        )

        return firefighterRepository
            .findAllByFiredepartmentFiredepartmentIdAndStatus(firedepartmentId, FirefighterStatus.ACTIVE, pageable)
            .map(firefighterMapper::toFirefighterDto)
            .toPageResponse()
    }

    @Transactional
    override fun updateFirefighter(
        emailAddress: String,
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse {

        val userModerator = userRepository.findByEmailOrThrow(emailAddress)

        val firefighterModerator = firefighterRepository.findByIdOrThrow(userModerator.userId!!)


        val userUpdated = userRepository.findByIdOrThrow(firefighterId)

        val firefighterUpdated = firefighterRepository.findByIdOrThrow(userUpdated.userId!!)


        val firedepartmentId = firefighterModerator.requireFiredepartmentId()

        firefighterUpdated.requireSameFiredepartment(firedepartmentId)

        validateStatus(firefighterDto.status)
        validateRole(firefighterDto.role)

        firefighterMapper.patchFirefighter(firefighterDto, firefighterUpdated)

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighterUpdated)
        )
    }

    @Transactional(readOnly = true)
    override fun getFirefighterByEmailAddress(emailAddress: String): FirefighterDtos.FirefighterResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    override fun getPendingFirefighters(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            FIREFIGHTER_ALLOWED_SORTS,
            "user.firstName,asc",
            200
        )

        return firefighterRepository
            .findAllByFiredepartmentFiredepartmentIdAndStatus(firedepartmentId, FirefighterStatus.PENDING, pageable)
            .map(firefighterMapper::toFirefighterDto)
            .toPageResponse()
    }

    private val FIREFIGHTER_ALLOWED_SORTS = setOf(
        "firefighterId",
        "user.firstName",
        "user.lastName",
        "firefighterRole",
        "user.userId",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createFirefighterDev(
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {

        val user = userRepository.findByIdOrThrow(firefighterDto.userId)

        firefighterRepository.assertNotExistsByUserId(user.userId!!)

        val firedepartment = firedepartmentRepository.findByIdOrThrow(firefighterDto.firedepartmentId)

        val firefighter = firefighterMapper.toFirefighterEntity(firefighterDto)

        firefighter.user = user
        firefighter.firedepartment = firedepartment
        firefighter.role = FirefighterRole.USER
        firefighter.status = FirefighterStatus.PENDING

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllFirefightersDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREFIGHTER_ALLOWED_SORTS,
            defaultSort = "firefighterId,asc",
            maxSize = 200
        )

        return firefighterRepository.findAll(pageable)
            .map(firefighterMapper::toFirefighterDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getFirefighterByIdDev(
        firefighterId: Int
    ): FirefighterDtos.FirefighterResponse {

        val firefighter = firefighterRepository.findByIdOrThrow(firefighterId)

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    @Transactional
    override fun updateFirefighterDev(
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse {

        val firefighter = firefighterRepository.findByIdOrThrow(firefighterId)

        firefighterMapper.patchFirefighter(firefighterDto, firefighter)

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }
}