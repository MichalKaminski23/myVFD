package com.vfd.server.services.implementations

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.Firefighter
import com.vfd.server.entities.FirefighterStatus
import com.vfd.server.entities.Role
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.FirefighterMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.FirefighterService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterServiceImplementation(
    private val firefighterRepository: FirefighterRepository,
    private val firefighterMapper: FirefighterMapper,
    private val userRepository: UserRepository,
    private val firedepartmentRepository: FiredepartmentRepository
) : FirefighterService {

    override fun createFirefighter(
        emailAddress: String,
        firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {
        TODO("Not yet implemented")
    }

    override fun getFirefighters(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> {
        TODO("Not yet implemented")
    }

    override fun updateFirefighter(
        emailAddress: String,
        firefighterId: Int,
        firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse {
        TODO("Not yet implemented")
    }

    @Transactional(readOnly = true)
    override fun getFirefighterByEmailAddress(emailAddress: String): FirefighterDtos.FirefighterResponse {

        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw ResourceNotFoundException("User", "email address", emailAddress)

        val firefighter = firefighterRepository.findById(user.userId!!)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", user.userId!!) }

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    override fun getPendingFirefighters(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> {
        TODO("Not yet implemented")
    }

//    @Transactional(readOnly = true)
//    override fun getPendingFirefighters(emailAddress: String): List<FirefighterDtos.FirefighterResponse> {
//
//        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
//            ?: throw ResourceNotFoundException("User", "email", emailAddress)
//
//        val firefighter = firefighterRepository.findById(user.userId!!)
//            .orElseThrow { ResourceNotFoundException("Firefighter", "userId", user.userId!!) }
//
//        return firefighterRepository
//            .findAllByFiredepartmentFiredepartmentIdAndStatus(
//                firefighter.firedepartment!!.firedepartmentId!!,
//                FirefighterStatus.PENDING
//            )
//            .map(firefighterMapper::toFirefighterDto)
//    }

//    @Transactional(readOnly = true)
//    override fun getFirefightersFromLoggedUser(
//        emailAddress: String
//    ): List<FirefighterDtos.FirefighterResponse> {
//
//        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
//            ?: throw ResourceNotFoundException("User", "email", emailAddress)
//
//        val firefighter = firefighterRepository.findById(user.userId!!)
//            .orElseThrow { ResourceNotFoundException("Firefighter", "userId", user.userId!!) }
//
//        return firefighterRepository
//            .findAllByFiredepartmentFiredepartmentIdAndStatus(
//                firefighter.firedepartment!!.firedepartmentId!!,
//                FirefighterStatus.ACTIVE
//            )
//            .map(firefighterMapper::toFirefighterDto)
//    }

    private val FIREFIGHTER_ALLOWED_SORTS = setOf(
        "firefighterId",
        "firstName",
        "lastName",
        "role",
        "user.userId",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createFirefighterDev(
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
        firefighter.role = Role.USER
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

        val firefighter = firefighterRepository.findById(firefighterId)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", firefighterId) }

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    @Transactional
    override fun updateFirefighterDev(
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