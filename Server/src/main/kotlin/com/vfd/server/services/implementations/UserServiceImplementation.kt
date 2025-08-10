package com.vfd.server.services.implementations

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.dtos.UserDtos
import com.vfd.server.entities.Address
import com.vfd.server.entities.User
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.UserService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class UserServiceImplementation(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val userMapper: UserMapper,
    private val addressMapper: AddressMapper
) : UserService {

    private val USER_ALLOWED_SORTS = setOf(
        "userId", "firstName", "lastName", "emailAddress", "createdAt", "loggedAt", "active"
    )

    @Transactional
    override fun createUser(userDto: UserDtos.UserCreate): UserDtos.UserResponse {
        val email = userDto.emailAddress.trim().lowercase()

        if (userRepository.existsByEmailAddressIgnoreCase(email)) {
            throw ResourceConflictException("User", "emailAddress", email)
        }

        userDto.phoneNumber?.trim()?.let { phone ->
            if (userRepository.existsByPhoneNumber(phone)) {
                throw ResourceConflictException("User", "phoneNumber", phone)
            }
        }

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        val entity: User = userMapper.toUserEntity(userDto).apply {
            createdAt = now
            loggedAt = now
            active = true
        }

        userDto.address?.let { addrCreate: AddressDtos.AddressCreate ->
            val addrEntity = addressRepository.save(addressMapper.toAddressEntity(addrCreate))
            entity.address = addrEntity
        }

        return userMapper.toUserDto(userRepository.save(entity))
    }

    @Transactional(readOnly = true)
    override fun getAllUsers(page: Int, size: Int, sort: String): Page<UserDtos.UserResponse> {
        val pageable = PaginationUtils.toPageRequest(
            page = page, size = size, sort = sort,
            allowedFields = USER_ALLOWED_SORTS, defaultSort = "createdAt,desc", maxSize = 200
        )
        return userRepository.findAll(pageable).map(userMapper::toUserDto)
    }

    @Transactional(readOnly = true)
    override fun getUserById(userId: Int): UserDtos.UserResponse {
        val entity = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User", "ID", userId) }
        return userMapper.toUserDto(entity)
    }

    @Transactional
    override fun updateUser(userId: Int, userDto: UserDtos.UserPatch): UserDtos.UserResponse {
        val entity = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User", "ID", userId) }

        userDto.emailAddress?.trim()?.lowercase()?.let { newEmail ->
            userRepository.findByEmailAddressIgnoreCase(newEmail)?.let { existing ->
                if (existing.userId != entity.userId) {
                    throw ResourceConflictException("User", "emailAddress", newEmail)
                }
            }
        }

        userDto.phoneNumber?.trim()?.let { newPhone ->
            userRepository.findByPhoneNumber(newPhone)?.let { existing ->
                if (existing.userId != entity.userId) {
                    throw ResourceConflictException("User", "phoneNumber", newPhone)
                }
            }
        }

        userMapper.patchUser(userDto, entity)

        userDto.address?.let { addrPatch ->
            var addr = entity.address
            if (addr == null) {
                addr = Address()
            }
            addressMapper.patchAddress(addrPatch, addr)
            entity.address = addressRepository.save(addr)
        }

        return userMapper.toUserDto(userRepository.save(entity))
    }
}
