package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Operations")
class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    var operationId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id", nullable = false)
    lateinit var firedepartment: Firedepartment

    @ManyToOne(optional = false)
    @JoinColumn(name = "operation_type", nullable = false)
    lateinit var operationType: OperationType

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    lateinit var address: Address

    @Column(name = "description", length = 512, nullable = false)
    lateinit var description: String

    @ManyToMany
    @JoinTable(
        name = "Participations",
        joinColumns = [JoinColumn(name = "operation_id")],
        inverseJoinColumns = [JoinColumn(name = "firefighter_id")]
    )
    var participants: MutableList<Firefighter> = mutableListOf()
}