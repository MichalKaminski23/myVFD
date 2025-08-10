package com.vfd.server.services

import com.vfd.server.dtos.AddressDtos
import org.springframework.data.domain.Page

interface AddressService {

    fun createAddress(addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse
    fun getAllAddresses(
        page: Int = 0,
        size: Int = 20,
        sort: String = "addressId,asc"
    ): Page<AddressDtos.AddressResponse>

    fun getAddressById(addressId: Int): AddressDtos.AddressResponse
    fun updateAddress(addressId: Int, addressDto: AddressDtos.AddressPatch): AddressDtos.AddressResponse
}