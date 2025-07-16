package com.vfd.server.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "FirefighterActivities")
class FirefighterActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firefighter_activity_id")
    var firefighterActivityId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firefighter_id", nullable = false)
    lateinit var firefighter: Firefighter

    @ManyToOne(optional = false)
    @JoinColumn(name = "firefighter_activity_type", nullable = false)
    lateinit var firefighterActivityType: FirefighterActivityType

    @Column(name = "activity_date")
    lateinit var activityDate: LocalDateTime

    @Column(name = "expiration_date", nullable = true)
    var expirationDate: LocalDateTime? = null

    @Column(name = "description", length = 512, nullable = false)
    lateinit var description: String
}