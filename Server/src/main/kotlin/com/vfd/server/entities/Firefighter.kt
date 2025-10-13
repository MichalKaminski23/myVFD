package com.vfd.server.entities

import jakarta.persistence.*
import org.hibernate.annotations.Formula

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
    var role: FirefighterRole? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 16)
    var status: FirefighterStatus = FirefighterStatus.PENDING

    @Formula(
        "(SELECT COALESCE(SUM( " +
                "  CASE WHEN o.operation_end IS NOT NULL AND o.operation_end > o.operation_date " +
                "       THEN EXTRACT(EPOCH FROM (o.operation_end - o.operation_date)) / 60 " +
                "       ELSE 0 END" +
                "), 0) " +
                " FROM Operations o " +
                " JOIN Participations p ON p.operation_id = o.operation_id " +
                " WHERE p.firefighter_id = firefighter_id)"
    )
    var totalMinutesOnOperations: Double? = null

    @Transient
    fun getHours(): Double = ((totalMinutesOnOperations ?: 0.0) / 60.0)
}