package com.vfd.server.repositories

import com.vfd.server.entities.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address, Int> {

    fun findByCountryAndVoivodeshipAndCityAndPostalCodeAndStreetAndHouseNumberAndApartNumber(
        country: String,
        voivodeship: String,
        city: String,
        postalCode: String,
        street: String,
        houseNumber: String,
        apartNumber: String?
    ): Address?
}