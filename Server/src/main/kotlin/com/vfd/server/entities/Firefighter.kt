package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Firefighters")
class Firefighter {

    @Id
    @Column(name = "firefighter_id")
    var firefighterId: Int? = null

    @OneToOne
    @MapsId
    @JoinColumn(name = "firefighter_id")
    var user: User? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id")
    var firedepartment: Firedepartment? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 16)
    var firefighterRole: FirefighterRole? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16)
    var status: FirefighterStatus = FirefighterStatus.PENDING
}