package com.vfd.server.services.implementations

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.entities.Address
import com.vfd.server.entities.Firedepartment
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.FiredepartmentMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.services.FiredepartmentService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FiredepartmentServiceImplementation(
    private val firedepartmentRepository: FiredepartmentRepository,
    private val firedepartmentMapper: FiredepartmentMapper,
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper
) : FiredepartmentService {

    private val FIREDEPARTMENT_ALLOWED_SORTS = setOf(
        "firedepartmentId",
        "name",
        "NRFS",
        "address.addressId"
    )

    @Transactional
    override fun createFiredepartment(
        firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse {

        val firedepartment: Firedepartment = firedepartmentMapper.toFiredepartmentEntity(firedepartmentDto)

        val address = addressMapper.toAddressEntity(firedepartmentDto.address)
        firedepartment.address = addressRepository.save(address)

        return firedepartmentMapper.toFiredepartmentDto(firedepartmentRepository.save(firedepartment))
    }

    @Transactional(readOnly = true)
    override fun getAllFiredepartments(
        page: Int,
        size: Int,
        sort: String
    ): Page<FiredepartmentDtos.FiredepartmentResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREDEPARTMENT_ALLOWED_SORTS,
            defaultSort = "firedepartmentId,asc",
            maxSize = 200
        )

        return firedepartmentRepository.findAll(pageable).map(firedepartmentMapper::toFiredepartmentDto)
    }

    @Transactional(readOnly = true)
    override fun getFiredepartmentById(
        firedepartmentId: Int
    ): FiredepartmentDtos.FiredepartmentResponse {

        val firedepartment = firedepartmentRepository.findById(firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", firedepartmentId) }

        return firedepartmentMapper.toFiredepartmentDto(firedepartment)
    }

    @Transactional
    override fun updateFiredepartment(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse {

        val firedepartment = firedepartmentRepository.findById(firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", firedepartmentId) }

        firedepartmentMapper.patchFiredepartment(firedepartmentDto, firedepartment)

        firedepartmentDto.address
            ?.let { addressPatch ->
                val address: Address = firedepartment.address ?: Address()
                addressMapper.patchAddress(addressPatch, address)
                firedepartment.address = addressRepository.save(address)
            }

        return firedepartmentMapper.toFiredepartmentDto(firedepartmentRepository.save(firedepartment))
    }
}