package com.vfd.server.repositories

import com.vfd.server.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByEmailAddress(email: String): User?
    fun existsByEmailAddressIgnoreCase(emailAddress: String): Boolean
    fun existsByPhoneNumber(phoneNumber: String): Boolean
    fun findByEmailAddressIgnoreCase(emailAddress: String): User?
    fun findByPhoneNumber(phoneNumber: String): User?
}