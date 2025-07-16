package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Firefighters")
class Firefighter {
    @Id
    @Column(name = "firefighter_id")
    var firefighterId: Int? = null

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "firefighter_id", nullable = false)
    lateinit var user: User

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id", nullable = false)
    lateinit var firedepartment: Firedepartment

    @Column(name = "role", length = 16, nullable = false)
    lateinit var role: String
}