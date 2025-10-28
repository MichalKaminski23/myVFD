package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.entities.FirefighterStatus
import com.vfd.server.exceptions.InvalidStatusException
import com.vfd.server.mappers.FirefighterActivityMapper
import com.vfd.server.repositories.FirefighterActivityRepository
import com.vfd.server.repositories.FirefighterActivityTypeRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.FirefighterActivityService
import com.vfd.server.shared.*
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterActivityServiceImplementation(
    private val firefighterActivityRepository: FirefighterActivityRepository,
    private val firefighterActivityMapper: FirefighterActivityMapper,
    private val firefighterRepository: FirefighterRepository,
    private val firefighterActivityTypeRepository: FirefighterActivityTypeRepository,
    private val userRepository: UserRepository,
    private val messageSource: MessageSource
) : FirefighterActivityService {

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

    @Transactional
    override fun createFirefighterActivity(
        emailAddress: String,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firefighterActivityType =
            firefighterActivityTypeRepository.findByIdOrThrow(firefighterActivityDto.firefighterActivityType)

        validateDates(firefighterActivityDto.activityDate, firefighterActivityDto.expirationDate)

        val firefighterActivity = firefighterActivityMapper.toFirefighterActivityEntity(firefighterActivityDto).apply {
            this.firefighter = firefighter
            this.firefighterActivityType = firefighterActivityType
            this.status = FirefighterStatus.PENDING
        }

        return firefighterActivityMapper.toFirefighterActivityDto(firefighterActivityRepository.save(firefighterActivity))
    }

    @Transactional(readOnly = true)
    override fun getFirefighterActivities(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firefighterId = firefighter.firefighterId!!

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            sorts,
            "name,asc",
            200
        )

        return firefighterActivityRepository.findAllByFirefighterFirefighterId(
            firefighterId,
            pageable
        ).map(firefighterActivityMapper::toFirefighterActivityDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getPendingFirefightersActivities(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            sorts,
            "name,asc",
            200
        )

        return firefighterActivityRepository
            .findAllByFirefighterFiredepartmentFiredepartmentIdAndStatus(
                firedepartmentId,
                FirefighterStatus.PENDING,
                pageable
            )
            .map(firefighterActivityMapper::toFirefighterActivityDto)
            .toPageResponse()
    }

    @Transactional
    override fun updateFirefighterActivity(
        emailAddress: String,
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        firefighterRepository.findByIdOrThrow(user.userId!!)

        val firefighterActivity = firefighterActivityRepository.findByIdOrThrow(firefighterActivityId)

        //firefighterActivity.requireSameFirefighter(firefighter.firefighterId!!)

        firefighterActivityMapper.patchFirefighterActivity(firefighterActivityDto, firefighterActivity)

        firefighterActivityDto.firefighterActivityType
            ?.takeIf { it != firefighterActivity.firefighterActivityType?.firefighterActivityType }
            ?.let { code ->
                val firefighterActivityType = firefighterActivityTypeRepository.findByIdOrThrow(code)
                firefighterActivity.firefighterActivityType = firefighterActivityType
            }

        val effectiveInspectionDate = firefighterActivityDto.activityDate ?: firefighterActivity.activityDate
        val effectiveExpirationDate = firefighterActivityDto.expirationDate ?: firefighterActivity.expirationDate

        validateDates(effectiveInspectionDate, effectiveExpirationDate)

        firefighterActivity.status = firefighterActivityDto.status?.let { FirefighterStatus.valueOf(it) }
            ?: firefighterActivity.status

        validateStatus(firefighterActivityDto.status)

        return firefighterActivityMapper.toFirefighterActivityDto(
            firefighterActivityRepository.save(firefighterActivity)
        )
    }

    @Transactional(readOnly = true)
    override fun getFirefightersActivities(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            sorts,
            "name,asc",
            200
        )

        return firefighterActivityRepository
            .findAllByFirefighterFiredepartmentFiredepartmentId(
                firedepartmentId,
                pageable
            )
            .map(firefighterActivityMapper::toFirefighterActivityDto)
            .toPageResponse()
    }

    private val sorts = setOf(
        "firefighterActivityId",
        "activityDate",
        "expirationDate",
        "firefighter.firefighterId",
        "firefighterActivityType.firefighterActivityType"
    )

    @Transactional
    override fun createFirefighterActivityDev(
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreateDev
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity = firefighterActivityMapper.toFirefighterActivityEntityDev(firefighterActivityDto)

        val firefighter = firefighterRepository.findByIdOrThrow(firefighterActivityDto.firefighterId)
        firefighterActivity.firefighter = firefighter

        val firefighterActivityType =
            firefighterActivityTypeRepository.findByIdOrThrow(firefighterActivityDto.firefighterActivityType)
        firefighterActivity.firefighterActivityType = firefighterActivityType

        firefighterActivity.status = FirefighterStatus.PENDING

        validateDates(firefighterActivityDto.activityDate, firefighterActivityDto.expirationDate)

        return firefighterActivityMapper.toFirefighterActivityDto(
            firefighterActivityRepository.save(firefighterActivity)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllFirefighterActivitiesDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "firefighterActivityId,asc",
            maxSize = 200
        )

        return firefighterActivityRepository.findAll(pageable)
            .map(firefighterActivityMapper::toFirefighterActivityDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getFirefighterActivityByIdDev(
        firefighterActivityId: Int
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity = firefighterActivityRepository.findByIdOrThrow(firefighterActivityId)

        return firefighterActivityMapper.toFirefighterActivityDto(firefighterActivity)
    }

    @Transactional
    override fun updateFirefighterActivityDev(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity = firefighterActivityRepository.findByIdOrThrow(firefighterActivityId)

        firefighterActivityMapper.patchFirefighterActivity(firefighterActivityDto, firefighterActivity)

        firefighterActivityDto.firefighterActivityType
            ?.let { code ->
                if (code != firefighterActivity.firefighterActivityType?.firefighterActivityType) {
                    val firefighterActivityType =
                        firefighterActivityTypeRepository.findByIdOrThrow(firefighterActivityDto.firefighterActivityType)
                    firefighterActivity.firefighterActivityType = firefighterActivityType
                }
            }

        val effectiveInspectionDate = firefighterActivityDto.activityDate ?: firefighterActivity.activityDate
        val effectiveExpirationDate = firefighterActivityDto.expirationDate ?: firefighterActivity.expirationDate

        validateDates(effectiveInspectionDate, effectiveExpirationDate)

        firefighterActivity.status = firefighterActivityDto.status?.let { FirefighterStatus.valueOf(it) }
            ?: firefighterActivity.status

        return firefighterActivityMapper.toFirefighterActivityDto(
            firefighterActivityRepository.save(firefighterActivity)
        )
    }
}