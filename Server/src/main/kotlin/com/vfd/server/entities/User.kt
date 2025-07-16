package com.vfd.server.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var userId: Int? = null

    @Column(name = "first_name", length = 128, nullable = false)
    lateinit var firstName: String

    @Column(name = "last_name", length = 128, nullable = false)
    lateinit var lastName: String

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    lateinit var address: Address

    @Column(name = "phone_number", length = 16, nullable = false, unique = true)
    lateinit var phoneNumber: String

    @Column(name = "email_address", length = 128, nullable = false, unique = true)
    lateinit var emailAddress: String

    @Column(name = "created_at", nullable = false)
    lateinit var createdAt: LocalDateTime

    @Column(name = "logged_at", nullable = false)
    lateinit var loggedAt: LocalDateTime

    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true

    @Column(name = "password_hash", length = 255, nullable = false)
    @JsonIgnore
    lateinit var passwordHash: String
}