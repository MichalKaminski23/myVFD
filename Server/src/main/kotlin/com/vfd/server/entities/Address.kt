package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Addresses")
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    var addressId: Int? = null

    @Column(name = "country", length = 64, nullable = false)
    lateinit var country: String

    @Column(name = "voivodeship", length = 64, nullable = false)
    lateinit var voivodeship: String

    @Column(name = "city", length = 64, nullable = false)
    lateinit var city: String

    @Column(name = "postal_code", length = 16, nullable = false)
    lateinit var postalCode: String

    @Column(name = "street", length = 64, nullable = false)
    lateinit var street: String

    @Column(name = "house_number", length = 8, nullable = true)
    var houseNumber: String? = null

    @Column(name = "apart_number", length = 8, nullable = true)
    var apartNumber: String? = null
}