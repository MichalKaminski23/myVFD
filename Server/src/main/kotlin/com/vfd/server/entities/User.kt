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

    @Column(name = "first_name", length = 128)
    var firstName: String? = null

    @Column(name = "last_name", length = 128)
    var lastName: String? = null

    @ManyToOne
    @JoinColumn(name = "address_id")
    var address: Address? = null

    @Column(name = "phone_number", length = 16, unique = true)
    var phoneNumber: String? = null

    @Column(name = "email_address", length = 128, unique = true)
    var emailAddress: String? = null

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null

    @Column(name = "logged_at")
    var loggedAt: LocalDateTime? = null

    @Column(name = "is_active")
    var active: Boolean = true

    @Column(name = "password_hash", length = 255)
    @JsonIgnore
    var passwordHash: String? = null
}