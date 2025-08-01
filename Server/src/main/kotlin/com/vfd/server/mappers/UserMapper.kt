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
    fun fromUserRegistrationDtoToUser(dto: UserRegistrationDto): User

    @Mapping(source = "active", target = "isActive")
    fun fromUserToUserInfoDto(user: User): UserInfoDto

    fun fromUserToUserModeratorDto(user: User): UserModeratorDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateUserFromUserInfoDto(dto: UserInfoDto, @MappingTarget user: User)
}