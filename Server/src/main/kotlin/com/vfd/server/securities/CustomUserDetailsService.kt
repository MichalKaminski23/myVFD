package com.vfd.server.securities

import com.vfd.server.repositories.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(emailAddress: String): UserDetails {
        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw BadCredentialsException("User with email $emailAddress not found.")

        return UserPrincipal(user)
    }
}