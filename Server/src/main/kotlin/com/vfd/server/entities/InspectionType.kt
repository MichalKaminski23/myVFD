package com.vfd.server.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "InspectionTypes")
class InspectionType {
    @Id
    @Column(name = "inspection_type", length = 16, unique = true)
    var inspectionType: String? = null

    @Column(name = "name", length = 64, nullable = false)
    lateinit var name: String
}