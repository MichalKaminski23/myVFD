package com.vfd.server.services

import com.vfd.server.dtos.FirefighterDto
import com.vfd.server.entities.Firefighter
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
    fun createFirefighter(userId: Int, firedepartmentId: Int, roleName: String): FirefighterDto {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val firedepartment = firedepartmentRepository.findById(firedepartmentId)
            .orElseThrow { IllegalArgumentException("Firedepartment not found") }

        // Znajdujemy rolę na podstawie nazwy
        val role = Role.valueOf(roleName.uppercase()) // Pobieramy rolę z Enum


        // Tworzymy nowego strażaka
        val firefighter = Firefighter().apply {
            this.user = user // Powiązanie strażaka z użytkownikiem (ID będzie takie same)
            this.firedepartment = firedepartment
            this.role = role
        }

        val savedFirefighter = firefighterRepository.save(firefighter)

        return firefighterMapper.fromFirefighterToFirefighterDto(savedFirefighter)
    }

    fun getFirefighterById(id: Int): FirefighterDto {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter not found") }

        return firefighterMapper.fromFirefighterToFirefighterDto(firefighter)
    }

    // Aktualizacja roli strażaka
    @Transactional
    fun updateFirefighterRole(id: Int, roleName: String): FirefighterDto {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter not found") }

        val role = Role.valueOf(roleName.uppercase()) // Pobieramy rolę z Enum

        firefighter.role = role

        val updatedFirefighter = firefighterRepository.save(firefighter)

        return firefighterMapper.fromFirefighterToFirefighterDto(updatedFirefighter)
    }

    // Usuwanie roli strażaka
    @Transactional
    fun removeRoleFromFirefighter(id: Int): FirefighterDto {
        val firefighter = firefighterRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Firefighter not found") }


        // Usuwamy rolę
        firefighter.role = Role.USER

        val updatedFirefighter = firefighterRepository.save(firefighter)

        return firefighterMapper.fromFirefighterToFirefighterDto(updatedFirefighter)
    }
}
