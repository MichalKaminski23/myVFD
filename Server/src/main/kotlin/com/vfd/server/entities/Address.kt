package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Addresses")
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    var addressId: Int? = null

    @Column(name = "country", length = 64)
    var country: String? = null

    @Column(name = "voivodeship", length = 64)
    var voivodeship: String? = null

    @Column(name = "city", length = 64, nullable = false)
    var city: String? = null

    @Column(name = "postal_code", length = 16)
    var postalCode: String? = null

    @Column(name = "street", length = 64)
    var street: String? = null

    @Column(name = "house_number", length = 8)
    var houseNumber: String? = null

    @Column(name = "apart_number", length = 8)
    var apartNumber: String? = null
}