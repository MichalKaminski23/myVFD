package com.vfd.server.mappers

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.entities.Address
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface AddressMapper {

    fun toAddressDto(address: Address): AddressDtos.AddressResponse

    @Mapping(target = "addressId", ignore = true)
    fun toAddressEntity(dto: AddressDtos.AddressCreate): Address

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchAddress(dto: AddressDtos.AddressPatch, @MappingTarget address: Address)
}
