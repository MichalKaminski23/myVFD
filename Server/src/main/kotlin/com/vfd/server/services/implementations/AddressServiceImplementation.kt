package com.vfd.server.services.implementations

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.entities.Address
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.services.AddressService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImplementation(
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper
) : AddressService {

    private val ADDRESS_ALLOWED_SORTS = setOf(
        "addressId", "country", "voivodeship",
        "city", "postalCode", "street", "apartNumber", "houseNumber"
    )

    @Transactional
    override fun createAddress(addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse {
        val address: Address = addressMapper.toAddressEntity(addressDto)
        return addressMapper.toAddressDto(addressRepository.save(address))
    }

    @Transactional(readOnly = true)
    override fun getAllAddresses(page: Int, size: Int, sort: String): Page<AddressDtos.AddressResponse> {
        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = ADDRESS_ALLOWED_SORTS,
            defaultSort = "addressId,asc",
            maxSize = 200
        )
        return addressRepository.findAll(pageable).map(addressMapper::toAddressDto)
    }

    @Transactional(readOnly = true)
    override fun getAddressById(addressId: Int): AddressDtos.AddressResponse {
        val address = addressRepository.findById(addressId)
            .orElseThrow { ResourceNotFoundException("Address", "id", addressId) }
        return addressMapper.toAddressDto(address)
    }

    @Transactional
    override fun updateAddress(addressId: Int, addressDto: AddressDtos.AddressPatch): AddressDtos.AddressResponse {
        val address = addressRepository.findById(addressId)
            .orElseThrow { ResourceNotFoundException("Address", "id", addressId) }
        addressMapper.patchAddress(addressDto, address)
        return addressMapper.toAddressDto(addressRepository.save(address))
    }
}