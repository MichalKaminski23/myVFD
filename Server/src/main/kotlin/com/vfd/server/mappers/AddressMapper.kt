package com.vfd.server.mappers

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.entities.Address
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface AddressMapper {

    fun toAddressDto(address: Address): AddressDtos.AddressResponse

    @Mapping(target = "addressId", ignore = true)
    fun toAddressEntity(addressDto: AddressDtos.AddressCreate): Address
}