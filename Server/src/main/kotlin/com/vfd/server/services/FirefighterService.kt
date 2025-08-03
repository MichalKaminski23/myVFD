package com.vfd.server.services

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.Role
import com.vfd.server.mappers.FirefighterMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FirefighterService(
    private val firefighterRepository: FirefighterRepository,
    private val userRepository: UserRepository,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val firefighterMapper: FirefighterMapper
) {

    @Transactional
    fun createFirefighter(dto: FirefighterDtos.FirefighterCreate): FirefighterDtos.FirefighterResponse {
        val user = userRepository.findById(dto.userId)
            .orElseThrow { IllegalArgumentException("User with ID ${dto.userId} not found") }

        val firedepartment = firedepartmentRepository.findById(dto.firedepartmentId)
            .orElseThrow { IllegalArgumentException("Firedepartment with ID ${dto.firedepartmentId} not found") }

        val firefighter = firefighterMapper.toFirefighterEntity(dto).apply {
            this.user = user
            this.firedepartment = firedepartment
        }

        return firefighterMapper.toFirefighterDto(firefighterRepository.save(firefighter))
    }

    fun getFirefighterById(id: Int): FirefighterDtos.FirefighterResponse {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter with ID $id not found") }

        return firefighterMapper.toFirefighterDto(firefighter)
    }

    @Transactional
    fun updateFirefighter(id: Int, dto: FirefighterDtos.FirefighterPatch): FirefighterDtos.FirefighterResponse {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter with ID $id not found") }

        firefighterMapper.patchFirefighter(dto, firefighter)

        dto.firedepartmentId?.let {
            val fd = firedepartmentRepository.findById(it)
                .orElseThrow { IllegalArgumentException("Firedepartment with ID $it not found") }
            firefighter.firedepartment = fd
        }

        return firefighterMapper.toFirefighterDto(firefighterRepository.save(firefighter))
    }

    @Transactional
    fun updateFirefighterRole(id: Int, roleName: String): FirefighterDtos.FirefighterResponse {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter with ID $id not found") }

        val role = runCatching { Role.valueOf(roleName.uppercase()) }
            .getOrElse { throw IllegalArgumentException("Invalid role name: $roleName") }

        firefighter.role = role

        return firefighterMapper.toFirefighterDto(firefighterRepository.save(firefighter))
    }

    @Transactional
    fun removeRoleFromFirefighter(id: Int): FirefighterDtos.FirefighterResponse {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter with ID $id not found") }

        firefighter.role = Role.USER

        return firefighterMapper.toFirefighterDto(firefighterRepository.save(firefighter))
    }
}
