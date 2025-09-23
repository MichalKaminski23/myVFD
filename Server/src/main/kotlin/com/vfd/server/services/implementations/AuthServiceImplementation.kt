package com.vfd.server.services

import com.vfd.server.dtos.AuthResponseDto
import com.vfd.server.dtos.UserDtos
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.securities.JwtTokenProvider
import com.vfd.server.securities.UserPrincipal
import com.vfd.server.services.implementations.AddressServiceImplementation
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

        if (userRepository.findByEmailAddressIgnoreCase(userDto.emailAddress) != null) {
            throw ResourceConflictException("User", "email address", userDto.emailAddress)
        }

        if (userRepository.findByPhoneNumber(userDto.phoneNumber) != null) {
            throw ResourceConflictException("User", "phone number", userDto.phoneNumber)
        }

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

        userRepository.findByEmailAddressIgnoreCase(userDto.emailAddress)
            ?: throw ResourceNotFoundException("User", "email address", userDto.emailAddress)

        val jwt = generateJwt(userDto.emailAddress, userDto.password)

        return AuthResponseDto(jwt)
    }
}