package com.vfd.server.services

import com.vfd.server.dtos.UserInfoDto
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val userMapper: UserMapper
) {
    fun getAllUsers(): List<UserInfoDto> =
        userRepository.findAll().map(userMapper::toInfoDto)


    fun getUserById(id: Int): UserInfoDto =
        userRepository.findById(id)
            .map(userMapper::toInfoDto)
            .orElseThrow { ResourceNotFoundException("User with id $id not found.") }

}