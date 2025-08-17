package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Firedepartments")
class Firedepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firedepartment_id")
    var firedepartmentId: Int? = null

    @Column(name = "name", length = 128)
    var name: String? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    var address: Address? = null

    @Column(name = "is_NRFS")
    var NRFS: Boolean = true
}