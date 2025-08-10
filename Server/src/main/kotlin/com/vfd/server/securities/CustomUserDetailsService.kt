package com.vfd.server.securities

import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmailAddress(email)
            ?: throw ResourceNotFoundException("User", "email", email)
        return org.springframework.security.core.userdetails.User(
            user.emailAddress,
            user.passwordHash,
            user.active, true, true, true,
            emptyList()
        )
    }
}
