package com.vfd.server.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "FirefighterActivityTypes")
class FirefighterActivityType {

    @Id
    @Column(name = "firefighter_activity_type", length = 16, unique = true)
    var firefighterActivityType: String? = null

    @Column(name = "name", length = 64)
    var name: String? = null
}