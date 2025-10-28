package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.FirefighterRole
import com.vfd.server.entities.FirefighterStatus
import com.vfd.server.exceptions.InvalidNumberException
import com.vfd.server.exceptions.InvalidStatusException
import com.vfd.server.exceptions.PresidentAlreadyExistsException
import com.vfd.server.mappers.FirefighterMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.FirefighterService
import com.vfd.server.shared.*
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class FirefighterServiceImplementation(
    private val firefighterRepository: FirefighterRepository,
    private val firefighterMapper: FirefighterMapper,
    private val userRepository: UserRepository,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val messageSource: MessageSource
) : FirefighterService {

    fun validateStatus(status: String?) {
        if (status == null) return
        try {
            FirefighterStatus.valueOf(status)
        } catch (_: IllegalArgumentException) {
            val locale = LocaleContextHolder.getLocale()
            val allowed = FirefighterStatus.entries.joinToString()
            val message = messageSource.getMessage(
                "invalid.status",
                arrayOf(status, allowed),
                "Invalid status: $status. Allowed: $allowed",
                locale
            )
            throw InvalidStatusException(message!!)
        }
    }

    fun validateRole(status: String?) {
        if (status == null) return
        try {
            FirefighterRole.valueOf(status)
        } catch (_: IllegalArgumentException) {
            val locale = LocaleContextHolder.getLocale()
            val allowed = FirefighterStatus.entries.joinToString()
            val message = messageSource.getMessage(
                "invalid.role",
                arrayOf(status, allowed),
                "Invalid role: $status. Allowed: $allowed",
                locale
            )
            throw InvalidStatusException(message!!)
        }
    }

    private val roles = setOf(
        FirefighterRole.ADMIN,
        FirefighterRole.PRESIDENT,
        FirefighterRole.MEMBER
    )

    @Transactional
    override fun createFirefighter(
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {

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

    override fun createFirefighterByEmailAddress(
        emailAddress: String,
        firefighterDto: FirefighterDtos.FirefighterCreateByEmailAddress
    ): FirefighterDtos.FirefighterResponse {

        val userAdmin = userRepository.findByEmailOrThrow(emailAddress)

        firefighterRepository.findByIdOrThrow(userAdmin.userId!!)


        val userCreated = userRepository.findByEmailOrThrow(firefighterDto.userEmailAddress)

        val firedepartment = firedepartmentRepository.findByIdOrThrow(firefighterDto.firedepartmentId)

        if (firefighterRepository.existsByFiredepartmentFiredepartmentIdAndRole(
                firedepartment.firedepartmentId!!,
                FirefighterRole.PRESIDENT
            )
        ) {
            throw PresidentAlreadyExistsException(firefighterDto.userEmailAddress)
        }

        val firefighter = firefighterMapper.toFirefighterEntityByEmailAddress(firefighterDto)


        firefighter.user = userCreated
        firefighter.firedepartment = firedepartment
        firefighter.role = FirefighterRole.PRESIDENT
        firefighter.status = FirefighterStatus.ACTIVE

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
            sorts,
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

        val newRole = FirefighterRole.valueOf(firefighterDto.role!!)
        if (newRole == FirefighterRole.PRESIDENT) {
            if (firefighterRepository.existsByFiredepartmentFiredepartmentIdAndRoleAndFirefighterIdNot(
                    firedepartmentId,
                    FirefighterRole.PRESIDENT,
                    firefighterUpdated.firefighterId!!
                )
            ) {
                throw PresidentAlreadyExistsException(userUpdated.emailAddress!!)
            }
        }

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
            sorts,
            "user.firstName,asc",
            200
        )

        return firefighterRepository
            .findAllByFiredepartmentFiredepartmentIdAndStatus(firedepartmentId, FirefighterStatus.PENDING, pageable)
            .map(firefighterMapper::toFirefighterDto)
            .toPageResponse()
    }

    @Transactional
    override fun deleteFirefighter(
        emailAddress: String,
        firefighterId: Int
    ) {
        val userModerator = userRepository.findByEmailOrThrow(emailAddress)

        val firefighterModerator = firefighterRepository.findByIdOrThrow(userModerator.userId!!)

        val userDeleted = userRepository.findByIdOrThrow(firefighterId)

        val firefighterDeleted = firefighterRepository.findByIdOrThrow(userDeleted.userId!!)

        val firedepartmentId = firefighterModerator.requireFiredepartmentId()

        firefighterDeleted.requireSameFiredepartment(firedepartmentId)

        if (roles.contains(firefighterDeleted.role)) {
            throw InvalidStatusException("Cannot delete firefighter with role ${firefighterDeleted.role}")
        }

        userDeleted.firefighter = null
        userRepository.saveAndFlush(userDeleted)
        firefighterDeleted.user = null
        firefighterRepository.save(firefighterDeleted)

        firefighterRepository.delete(firefighterDeleted)
    }

    @Transactional(readOnly = true)
    override fun getHoursForQuarter(emailAddress: String, year: Int, quarter: Int): Double {

        val user = userRepository.findByEmailOrThrow(emailAddress)
        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        if (quarter !in 1..4) {
            throw InvalidNumberException("Quarter must be between 1 and 4")
        }

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        if (year !in 0..now.year) {
            throw InvalidNumberException("Year must be between 0 and can't be grater than today")
        }

        return firefighterRepository.getHoursForQuarter(firefighter.firefighterId!!, year, quarter)
    }

    private val sorts = setOf(
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
            allowedFields = sorts,
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

        val newRole = firefighterDto.role?.let { FirefighterRole.valueOf(it) }
        if (newRole == FirefighterRole.PRESIDENT) {
            val fdId = firefighter.firedepartment?.firedepartmentId
                ?: throw InvalidStatusException("Firefighter is not assigned to any firedepartment")
            if (firefighterRepository.existsByFiredepartmentFiredepartmentIdAndRoleAndFirefighterIdNot(
                    fdId,
                    FirefighterRole.PRESIDENT,
                    firefighter.firefighterId!!
                )
            ) {
                throw PresidentAlreadyExistsException(firefighter.user?.emailAddress!!)
            }
        }

        firefighterMapper.patchFirefighter(firefighterDto, firefighter)

        return firefighterMapper.toFirefighterDto(
            firefighterRepository.save(firefighter)
        )
    }
}