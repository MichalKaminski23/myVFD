package com.vfd.server.services

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserLoginDto
import com.vfd.server.dtos.UserRegistrationDto
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.securities.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun register(dto: UserRegistrationDto): AuthResponseDto {
        val address = addressRepository.save(addressMapper.toEntity(dto.address))

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val userEntity = userMapper.registrationDtoToUser(dto).apply {
            this.address = address
            this.passwordHash = passwordEncoder.encode(dto.password)
            this.createdAt = now
            this.loggedAt = now
            this.isActive = true
        }
        userRepository.save(userEntity)

        val authToken = UsernamePasswordAuthenticationToken(dto.emailAddress, dto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }

    fun login(dto: UserLoginDto): AuthResponseDto {
        val authToken = UsernamePasswordAuthenticationToken(dto.emailAddress, dto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }
}
