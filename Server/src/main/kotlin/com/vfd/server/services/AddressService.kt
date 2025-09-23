package com.vfd.server.services

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.entities.Address
import com.vfd.server.shared.PageResponse

interface AddressService {

    fun createAddressDev(addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse

    fun getAllAddressesDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "addressId,asc"
    ): PageResponse<AddressDtos.AddressResponse>

    fun getAddressByIdDev(addressId: Int): AddressDtos.AddressResponse

    fun findOrCreateAddress(addressDto: AddressDtos.AddressCreate): Address
}