package com.vfd.server.mappers

import com.vfd.server.dtos.UserInfoDto
import com.vfd.server.dtos.UserModeratorDto
import com.vfd.server.dtos.UserRegistrationDto
import com.vfd.server.entities.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = [AddressMapper::class]
)
interface UserMapper {
    @Mapping(source = "address", target = "address")
    //@Mapping(source = "password", target = "passwordHash")
    fun registrationDtoToUser(dto: UserRegistrationDto): User

    @Mapping(source = "address.addressId", target = "addressId")
    @Mapping(source = "active", target = "isActive")
    fun toInfoDto(user: User): UserInfoDto

    fun toModeratorDto(user: User): UserModeratorDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateEntityFromInfoDto(dto: UserInfoDto, @MappingTarget user: User)
}