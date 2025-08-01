package com.vfd.server.mappers

import com.vfd.server.dtos.AddressDto
import com.vfd.server.entities.Address
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface AddressMapper {

    fun fromAddressDtoToAddress(addressDto: AddressDto): Address

    fun fromAddressToAddressDto(address: Address): AddressDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateAddressFromAddressDto(addressDto: AddressDto, @MappingTarget address: Address)
}