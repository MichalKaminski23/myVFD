package com.vfd.server.services

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserDtos
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
    fun register(dto: UserDtos.UserCreate): AuthResponseDto {
        val address = addressRepository.save(addressMapper.toAddressEntity(dto.address))

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val userEntity = userMapper.toUserEntity(dto).apply {
            this.address = address
            this.passwordHash = passwordEncoder.encode(dto.password)
            this.createdAt = now
            this.loggedAt = now
            this.active = true
        }
        userRepository.save(userEntity)

        val authToken = UsernamePasswordAuthenticationToken(dto.emailAddress, dto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }

    fun login(dto: UserDtos.UserLogin): AuthResponseDto {
        val authToken = UsernamePasswordAuthenticationToken(dto.emailAddress, dto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }
}
