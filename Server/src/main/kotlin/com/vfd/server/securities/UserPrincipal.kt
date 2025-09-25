package com.vfd.server.securities

import com.vfd.server.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserPrincipal(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val role = user.firefighter?.firefighterRole?.name ?: "USER"
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword() = user.passwordHash
    override fun getUsername() = user.emailAddress

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = user.active
}