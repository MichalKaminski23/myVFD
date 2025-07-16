package com.vfd.server.mappers

import com.vfd.server.dtos.AddressDto
import com.vfd.server.dtos.UserDto
import com.vfd.server.entities.Address
import com.vfd.server.entities.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toDto(user: User): UserDto
    fun toDto(address: Address): AddressDto
}