package com.vfd.server.services

import com.vfd.server.dtos.UserDtos
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val userMapper: UserMapper,
    private val addressMapper: AddressMapper
) {

    fun getAllUsers(): List<UserDtos.UserResponse> =
        userRepository.findAll().map(userMapper::toUserDto)

    fun getUserById(id: Int): UserDtos.UserResponse =
        userRepository.findById(id)
            .map(userMapper::toUserDto)
            .orElseThrow { ResourceNotFoundException("User with id $id not found.") }

    @Transactional
    fun updateUser(id: Int, dto: UserDtos.UserPatch): UserDtos.UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User with id $id not found.") }

        userMapper.patchUser(dto, user)

//        dto.address?.let {
//            val updatedAddress = addressMapper.fromCreate(
//                addressMapper.toResponse(user.address!!).copy(
//                    country = it.country ?: user.address!!.country,
//                    voivodeship = it.voivodeship ?: user.address!!.voivodeship,
//                    city = it.city ?: user.address!!.city,
//                    postalCode = it.postalCode ?: user.address!!.postalCode,
//                    street = it.street ?: user.address!!.street,
//                    houseNumber = it.houseNumber ?: user.address!!.houseNumber,
//                    apartNumber = it.apartNumber ?: user.address!!.apartNumber
//                )
//            )
//            user.address = addressRepository.save(updatedAddress)
//        }

        return userMapper.toUserDto(userRepository.save(user))
    }

    @Transactional
    fun deleteUser(id: Int) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("User with id $id not found.")
        }
        userRepository.deleteById(id)
    }
}
