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

    @ManyToOne
    @JoinColumn(name = "asset_id")
    var asset: Asset? = null

    @ManyToOne
    @JoinColumn(name = "inspection_type")
    var inspectionType: InspectionType? = null

    @Column(name = "inspection_date")
    var inspectionDate: LocalDateTime? = null

    @Column(name = "date_expiration")
    var expirationDate: LocalDateTime? = null
}