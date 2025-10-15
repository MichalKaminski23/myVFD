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

    @ManyToOne
    @JoinColumn(name = "firefighter_id")
    var firefighter: Firefighter? = null

    @ManyToOne
    @JoinColumn(name = "firefighter_activity_type")
    var firefighterActivityType: FirefighterActivityType? = null

    @Column(name = "activity_date")
    var activityDate: LocalDateTime? = null

    @Column(name = "expiration_date")
    var expirationDate: LocalDateTime? = null

    @Column(name = "description", length = 512)
    var description: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16)
    var status: FirefighterStatus = FirefighterStatus.PENDING
}