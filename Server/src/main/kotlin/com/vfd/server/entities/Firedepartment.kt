package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Firedepartments")
class Firedepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firedepartment_id")
    var firedepartmentId: Int? = null

    @Column(name = "name", length = 128, nullable = false)
    lateinit var name: String

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    lateinit var address: Address

    @Column(name = "is_NRFS", nullable = false)
    var isNRFS: Boolean = true

    @OneToMany(mappedBy = "firedepartment", fetch = FetchType.LAZY)
    val firefighters: List<Firefighter> = emptyList()
}