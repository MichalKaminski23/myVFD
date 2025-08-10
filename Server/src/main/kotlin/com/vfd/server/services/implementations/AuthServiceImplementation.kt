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
class AuthServiceImplementation(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper,
    private val userMapper: UserMapper,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthService {

    @Transactional
    override fun register(userDto: UserDtos.UserCreate): AuthResponseDto {
        val address = addressRepository.save(addressMapper.toAddressEntity(userDto.address))

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val user = userMapper.toUserEntity(userDto).apply {
            this.address = address
            this.passwordHash = passwordEncoder.encode(userDto.password)
            this.createdAt = now
            this.loggedAt = now
            this.active = true
        }
        userRepository.save(user)

        val authToken = UsernamePasswordAuthenticationToken(userDto.emailAddress, userDto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }

    @Transactional
    override fun login(userDto: UserDtos.UserLogin): AuthResponseDto {
        val authToken = UsernamePasswordAuthenticationToken(userDto.emailAddress, userDto.password)
        val auth = authenticationManager.authenticate(authToken)
        val jwt = jwtTokenProvider.generateToken(auth)
        return AuthResponseDto(jwt)
    }
}