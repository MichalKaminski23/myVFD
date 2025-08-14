package com.vfd.server.services

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.shared.PageResponse

interface AddressService {

    fun createAddress(addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse

    fun getAllAddresses(
        page: Int = 0,
        size: Int = 20,
        sort: String = "addressId,asc"
    ): PageResponse<AddressDtos.AddressResponse>

    fun getAddressById(addressId: Int): AddressDtos.AddressResponse

    fun updateAddress(addressId: Int, addressDto: AddressDtos.AddressPatch): AddressDtos.AddressResponse
}