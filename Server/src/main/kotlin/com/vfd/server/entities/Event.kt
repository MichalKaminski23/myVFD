package com.vfd.server.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Events")
class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    var eventId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id")
    var firedepartment: Firedepartment? = null

    @Column(name = "header", length = 128)
    var header: String? = null

    @Column(name = "description", length = 1023)
    var description: String? = null

    @Column(name = "event_date")
    var eventDate: LocalDateTime? = null
}