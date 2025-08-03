package com.vfd.server.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "OperationTypes")
class OperationType {

    @Id
    @Column(name = "operation_type", length = 16, unique = true)
    var operationType: String? = null

    @Column(name = "name", length = 64)
    var name: String? = null
}