package com.vfd.server.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Inspections")
class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_id")
    var inspectionId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    lateinit var asset: Asset

    @ManyToOne(optional = false)
    @JoinColumn(name = "inspection_type", nullable = false)
    lateinit var inspectionType: InspectionType

    @Column(name = "inspection_date", nullable = false)
    lateinit var inspectionDate: LocalDateTime

    @Column(name = "expiration_date", nullable = true)
    var expirationDate: LocalDateTime? = null
}