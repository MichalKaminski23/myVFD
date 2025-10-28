package com.vfd.server.services.implementations

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.PasswordDtos
import com.vfd.server.dtos.UserDtos
import com.vfd.server.exceptions.InvalidPasswordException
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.UserRepository
import com.vfd.server.securities.JwtTokenProvider
import com.vfd.server.securities.UserPrincipal
import com.vfd.server.services.AuthService
import com.vfd.server.shared.assertNotExistsByEmail
import com.vfd.server.shared.assertNotExistsByPhone
import com.vfd.server.shared.findByEmailOrThrow
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class AuthServiceImplementation(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val addressService: AddressServiceImplementation
) : AuthService {

    override fun generateJwt(emailAddress: String, password: String): String {
        val authToken = UsernamePasswordAuthenticationToken(emailAddress, password)
        val auth = authenticationManager.authenticate(authToken)
        return jwtTokenProvider.generateToken(auth)
    }

    @Transactional
    override fun register(userDto: UserDtos.UserCreate): AuthResponseDto {

        userRepository.assertNotExistsByEmail(userDto.emailAddress)

        userRepository.assertNotExistsByPhone(userDto.phoneNumber)

        val address = addressService.findOrCreateAddress(userDto.address)

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        val user = userMapper.toUserEntity(userDto).apply {
            this.address = address
            this.passwordHash = passwordEncoder.encode(userDto.password)
            this.createdAt = now
            this.loggedAt = now
            this.active = true
        }

        userRepository.saveAndFlush(user)

        val principal = UserPrincipal(user)
        val auth = UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
        val jwt = jwtTokenProvider.generateToken(auth)

        return AuthResponseDto(jwt)
    }

    @Transactional
    override fun login(userDto: UserDtos.UserLogin): AuthResponseDto {

        userRepository.findByEmailOrThrow(userDto.emailAddress)

        val jwt = generateJwt(userDto.emailAddress, userDto.password)

        return AuthResponseDto(jwt)
    }

    @Transactional
    override fun changePassword(emailAddress: String, passwordDto: PasswordDtos.PasswordChange) {
        val user = userRepository.findByEmailOrThrow(emailAddress)

        if (!passwordEncoder.matches(passwordDto.currentPassword, user.passwordHash)) {
            throw InvalidPasswordException("Current password is invalid.")
        }

        if (passwordDto.currentPassword == passwordDto.newPassword) {
            throw InvalidPasswordException("New password must be different than current password.")
        }

        user.passwordHash = passwordEncoder.encode(passwordDto.newPassword)
        userRepository.save(user)
    }
}