package com.vfd.server.mappers

import com.vfd.server.dtos.AddressDto
import com.vfd.server.entities.Address
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface AddressMapper {
    fun toDto(address: Address): AddressDto
    
    fun toEntity(dto: AddressDto): Address

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateEntityFromDto(dto: AddressDto, @MappingTarget address: Address)
}