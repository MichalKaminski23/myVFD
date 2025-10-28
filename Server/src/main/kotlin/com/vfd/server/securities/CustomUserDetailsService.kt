package com.vfd.server.securities

import com.vfd.server.repositories.UserRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val messageSource: MessageSource
) : UserDetailsService {

    override fun loadUserByUsername(emailAddress: String): UserDetails {
        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw BadCredentialsException(
                messageSource.getMessage(
                    "user.not_found",
                    arrayOf(emailAddress),
                    "User with email $emailAddress not found",
                    LocaleContextHolder.getLocale()
                )
            )

        return UserPrincipal(user)
    }
}