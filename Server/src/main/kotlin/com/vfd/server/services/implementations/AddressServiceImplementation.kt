package com.vfd.server.services.implementations

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.entities.Address
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.services.AddressService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.findByIdOrThrow
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImplementation(
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper
) : AddressService {

    @Transactional
    override fun findOrCreateAddress(addressDto: AddressDtos.AddressCreate): Address {

        val existingAddress = addressRepository
            .findByCountryAndVoivodeshipAndCityAndPostalCodeAndStreetAndHouseNumberAndApartNumber(
                country = addressDto.country,
                voivodeship = addressDto.voivodeship,
                city = addressDto.city,
                postalCode = addressDto.postalCode,
                street = addressDto.street,
                houseNumber = addressDto.houseNumber,
                apartNumber = addressDto.apartNumber
            )

        return existingAddress ?: addressRepository.save(addressMapper.toAddressEntity(addressDto))
    }

    private val sorts = setOf(
        "addressId", "country", "voivodeship",
        "city", "postalCode", "street", "apartNumber", "houseNumber"
    )

    @Transactional
    override fun createAddressDev(addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse {

        val address = addressMapper.toAddressEntity(addressDto)

        return addressMapper.toAddressDto(addressRepository.save(address))
    }

    @Transactional(readOnly = true)
    override fun getAllAddressesDev(page: Int, size: Int, sort: String): PageResponse<AddressDtos.AddressResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "addressId,asc",
            maxSize = 200
        )

        return addressRepository.findAll(pageable).map(addressMapper::toAddressDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getAddressByIdDev(addressId: Int): AddressDtos.AddressResponse {

        val address = addressRepository.findByIdOrThrow(addressId)

        return addressMapper.toAddressDto(address)
    }
}