package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Operations")
class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    var operationId: Int? = null

    @ManyToOne
    @JoinColumn(name = "firedepartment_id")
    var firedepartment: Firedepartment? = null

    @ManyToOne
    @JoinColumn(name = "operation_type")
    var operationType: OperationType? = null

    @ManyToOne
    @JoinColumn(name = "address_id")
    var address: Address? = null

    @Column(name = "description", length = 512)
    var description: String? = null

    @ManyToMany
    @JoinTable(
        name = "Participations",
        joinColumns = [JoinColumn(name = "operation_id")],
        inverseJoinColumns = [JoinColumn(name = "firefighter_id")]
    )
    var participants: MutableList<Firefighter> = mutableListOf()
}