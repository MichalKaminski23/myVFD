package com.vfd.server.securities

import com.vfd.server.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(emailAddress: String): UserDetails {
        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw UsernameNotFoundException("User with email $emailAddress not found.")

        return UserPrincipal(user)
    }
}