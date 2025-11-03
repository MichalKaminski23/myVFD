package com.vfd.server.configs

import com.vfd.server.entities.Address
import com.vfd.server.entities.Firedepartment
import com.vfd.server.entities.Firefighter
import com.vfd.server.entities.User
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Profile("!test")
class AdminInitializer(
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository,
    private val passwordEncoder: PasswordEncoder,
    private val entityManager: EntityManager,
    @param:Value("\${spring.datasource.password}") val password: String,
) : ApplicationRunner {

    companion object {
        private val logger = LoggerFactory.getLogger(AdminInitializer::class.java)
    }

    @Transactional
    override fun run(args: ApplicationArguments) {
        val adminEmailAddress = "localAdmin@mojeOSP.pl"

        if (userRepository.existsByEmailAddress(adminEmailAddress)) {
            logger.info("ℹ️ Admin user already exists: {}", adminEmailAddress)
            return
        }

        val address = Address().apply {
            country = "Polska"
            voivodeship = "Slaskie"
            city = "Strzyzowice"
            postalCode = "69-420"
            street = "Belna"
            houseNumber = "1"
            apartNumber = null
        }

        entityManager.persist(address)

        val admin = User().apply {
            firstName = "Admin"
            lastName = "Local"
            emailAddress = adminEmailAddress
            phoneNumber = "+48111222333"
            passwordHash = passwordEncoder.encode(password)
            this.address = address
            createdAt = LocalDateTime.now()
            loggedAt = LocalDateTime.now()
            active = true
        }

        val firedepartment = Firedepartment().apply {
            name = "ADMIN - DON'T USE"
            this.address = address
            nrfs = true
        }

        entityManager.persist(firedepartment)

        val adminFirefighter = Firefighter().apply {
            this.user = admin
            this.firedepartment = firedepartment
            role = com.vfd.server.entities.FirefighterRole.ADMIN
            status = com.vfd.server.entities.FirefighterStatus.ACTIVE
        }

        userRepository.save(admin)
        firefighterRepository.save(adminFirefighter)
        logger.info("✅ Default admin user created: {}", adminEmailAddress)
    }
}