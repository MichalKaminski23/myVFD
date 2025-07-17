//package com.vfd.server.services
//
//import com.vfd.server.dtos.UserInfoDto
//import com.vfd.server.entities.Address
//import com.vfd.server.entities.User
//import com.vfd.server.mappers.UserMapper
//import com.vfd.server.repositories.AddressRepository
//import com.vfd.server.repositories.UserRepository
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import java.time.LocalDateTime
//
//@Service
//class UserService(
//    private val userRepository: UserRepository,
//    private val addressRepository: AddressRepository,
//    private val userMapper: UserMapper
//) {
//    fun findAll(): List<UserInfoDto> =
//        userRepository.findAll().map(userMapper::toDto)
//
//    fun findById(id: Int): UserInfoDto =
//        userRepository.findById(id)
//            .map(userMapper::toDto)
//            .orElseThrow { NoSuchElementException("User $id not found") }
//
//    @Transactional
//    fun create(dto: UserInfoDto): UserInfoDto {
//        val addressEntity = Address().apply {
//            country = dto.address.country
//            voivodeship = dto.address.voivodeship
//            city = dto.address.city
//            postalCode = dto.address.postalCode
//            street = dto.address.street
//            houseNumber = dto.address.houseNumber
//            apartNumber = dto.address.apartNumber
//        }
//        addressRepository.save(addressEntity)
//
//        // 2. przygotuj User
//        val now = LocalDateTime.now()
//        val userEntity = User().apply {
//            firstName = dto.firstName
//            lastName = dto.lastName
//            address = addressEntity        // <-- nowy Address
//            phoneNumber = dto.phoneNumber
//            emailAddress = dto.emailAddress
//            createdAt = now
//            loggedAt = now
//            isActive = true
//            passwordHash = "dddddd" // TODO: haszowanie hasła
//        }
//
//        // 3. zapisz User i zwróć DTO
//        val savedUser = userRepository.save(userEntity)
//        return userMapper.toDto(savedUser)
//    }
//}