package com.vfd.server.services.implementations

import com.vfd.server.dtos.UserDtos
import com.vfd.server.exceptions.ResourceConflictException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.UserMapper
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.UserService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImplementation(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun updateUser(
        emailAddress: String,
        userDto: UserDtos.UserPatch
    ): UserDtos.UserResponse {
        TODO("Not yet implemented")
    }

    @Transactional(readOnly = true)
    override fun getUserByEmailAddress(emailAddress: String): UserDtos.UserResponse {

        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw ResourceNotFoundException("User", "email address", emailAddress)

        return userMapper.toUserDto(user)
    }

    private val USER_ALLOWED_SORTS = setOf(
        "userId",
        "firstName",
        "lastName",
        "emailAddress",
        "createdAt",
        "loggedAt",
        "active",
        "address.addressId"
    )

    @Transactional(readOnly = true)
    override fun getAllUsersDev(page: Int, size: Int, sort: String): PageResponse<UserDtos.UserResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = USER_ALLOWED_SORTS,
            defaultSort = "userId,asc",
            maxSize = 200
        )

        return userRepository.findAll(pageable)
            .map(userMapper::toUserDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getUserByIdDev(userId: Int): UserDtos.UserResponse {

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User", "id", userId) }

        return userMapper.toUserDto(user)
    }

    @Transactional
    override fun updateUserDev(userId: Int, userDto: UserDtos.UserPatch): UserDtos.UserResponse {

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User", "id", userId) }

        userDto.emailAddress?.trim()?.lowercase()?.let { newEmail ->
            userRepository.findByEmailAddressIgnoreCase(newEmail)?.let { existing ->
                if (existing.userId != user.userId) {
                    throw ResourceConflictException("User", "email address", newEmail)
                }
            }
        }

        userDto.phoneNumber?.trim()?.let { newPhone ->
            userRepository.findByPhoneNumber(newPhone)?.let { existing ->
                if (existing.userId != user.userId) {
                    throw ResourceConflictException("User", "phone number", newPhone)
                }
            }
        }

        userMapper.patchUser(userDto, user)

        return userMapper.toUserDto(
            userRepository.save(user)
        )
    }
}