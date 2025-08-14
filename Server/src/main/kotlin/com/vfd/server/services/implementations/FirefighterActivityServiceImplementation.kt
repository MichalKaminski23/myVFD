package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.entities.FirefighterActivity
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.FirefighterActivityMapper
import com.vfd.server.repositories.FirefighterActivityRepository
import com.vfd.server.repositories.FirefighterActivityTypeRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.services.FirefighterActivityService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterActivityServiceImplementation(
    private val firefighterActivityRepository: FirefighterActivityRepository,
    private val firefighterActivityMapper: FirefighterActivityMapper,
    private val firefighterRepository: FirefighterRepository,
    private val firefighterActivityTypeRepository: FirefighterActivityTypeRepository
) : FirefighterActivityService {

    private val FIREFIGHTER_ACTIVITY_ALLOWED_SORTS = setOf(
        "firefighterActivityId",
        "activityDate",
        "expirationDate",
        "firefighter.firefighterId",
        "firefighterActivityType.firefighterActivityType"
    )

    @Transactional
    override fun createFirefighterActivity(
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity: FirefighterActivity =
            firefighterActivityMapper.toFirefighterActivityEntity(firefighterActivityDto)

        val firefighter = firefighterRepository.findById(firefighterActivityDto.firefighterId)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", firefighterActivityDto.firefighterId) }
        firefighterActivity.firefighter = firefighter

        val firefighterActivityType =
            firefighterActivityTypeRepository.findById(firefighterActivityDto.firefighterActivityType)
                .orElseThrow {
                    ResourceNotFoundException(
                        "FirefighterActivityType",
                        "code",
                        firefighterActivityDto.firefighterActivityType
                    )
                }
        firefighterActivity.firefighterActivityType = firefighterActivityType

        return firefighterActivityMapper.toFirefighterActivityDto(
            firefighterActivityRepository.save(firefighterActivity)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllFirefighterActivities(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREFIGHTER_ACTIVITY_ALLOWED_SORTS,
            defaultSort = "firefighterActivityId,asc",
            maxSize = 200
        )

        return firefighterActivityRepository.findAll(pageable)
            .map(firefighterActivityMapper::toFirefighterActivityDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getFirefighterActivityById(
        firefighterActivityId: Int
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity = firefighterActivityRepository.findById(firefighterActivityId)
            .orElseThrow { ResourceNotFoundException("Firefighter's activity", "id", firefighterActivityId) }

        return firefighterActivityMapper.toFirefighterActivityDto(firefighterActivity)
    }

    @Transactional
    override fun updateFirefighterActivity(
        firefighterActivityId: Int,
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse {

        val firefighterActivity = firefighterActivityRepository.findById(firefighterActivityId)
            .orElseThrow { ResourceNotFoundException("Firefighter's activity", "id", firefighterActivityId) }

        firefighterActivityMapper.patchFirefighterActivity(firefighterActivityDto, firefighterActivity)

        firefighterActivityDto.firefighterActivityType
            ?.let { code ->
                if (code != firefighterActivity.firefighterActivityType?.firefighterActivityType) {
                    val firefighterActivityType = firefighterActivityTypeRepository.findById(code)
                        .orElseThrow {
                            ResourceNotFoundException(
                                "Firefighter activitie's type",
                                "code",
                                code
                            )
                        }
                    firefighterActivity.firefighterActivityType = firefighterActivityType
                }
            }

        return firefighterActivityMapper.toFirefighterActivityDto(
            firefighterActivityRepository.save(firefighterActivity)
        )
    }
}