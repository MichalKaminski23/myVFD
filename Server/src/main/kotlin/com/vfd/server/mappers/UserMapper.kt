package com.vfd.server.mappers

import com.vfd.server.dtos.UserDtos
import com.vfd.server.entities.User
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = [AddressMapper::class]
)
interface UserMapper {

    fun toUserDto(user: User): UserDtos.UserResponse

    fun toUserModeratorDto(user: User): UserDtos.UserModeratorResponse

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "loggedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    fun toUserEntity(userDto: UserDtos.UserCreate): User

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", ignore = true)
    fun patchUser(userDto: UserDtos.UserPatch, @MappingTarget user: User)
}