package com.vfd.server.services.implementations

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.mappers.FiredepartmentMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.services.FiredepartmentService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FiredepartmentServiceImplementation(
    private val firedepartmentRepository: FiredepartmentRepository,
    private val firedepartmentMapper: FiredepartmentMapper,
    private val addressRepository: AddressRepository,
    private val addressService: AddressServiceImplementation
) : FiredepartmentService {

    override fun createFiredepartment(
        emailAddress: String,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse {
        TODO("Not yet implemented")
    }

    override fun getFiredepartmentsShort(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponseShort> {
        TODO("Not yet implemented")
    }

    override fun getFiredepartment(emailAddress: String): FiredepartmentDtos.FiredepartmentResponse {
        TODO("Not yet implemented")
    }

    override fun updateFiredepartment(
        emailAddress: String,
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse {
        TODO("Not yet implemented")
    }

    private val FIREDEPARTMENT_ALLOWED_SORTS = setOf(
        "firedepartmentId",
        "name",
        "nrfs",
        "address.addressId"
    )

    @Transactional
    override fun createFiredepartmentDev(
        firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse {

        firedepartmentRepository.assertNotExistsByName(firedepartmentDto.name)

        val firedepartment = firedepartmentMapper.toFiredepartmentEntity(firedepartmentDto)

        val address = addressService.findOrCreateAddress(firedepartmentDto.address)
        firedepartment.address = addressRepository.save(address)

        return firedepartmentMapper.toFiredepartmentDto(firedepartmentRepository.save(firedepartment))
    }

    @Transactional(readOnly = true)
    override fun getAllFiredepartmentsDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = FIREDEPARTMENT_ALLOWED_SORTS,
            defaultSort = "firedepartmentId,asc",
            maxSize = 200
        )

        return firedepartmentRepository.findAll(pageable).map(firedepartmentMapper::toFiredepartmentDto)
            .toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getFiredepartmentByIdDev(
        firedepartmentId: Int
    ): FiredepartmentDtos.FiredepartmentResponse {

        val firedepartment = firedepartmentRepository.findByIdOrThrow(firedepartmentId)

        return firedepartmentMapper.toFiredepartmentDto(firedepartment)
    }

    @Transactional
    override fun updateFiredepartmentDev(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse {

        val firedepartment = firedepartmentRepository.findByIdOrThrow(firedepartmentId)

        firedepartmentMapper.patchFiredepartment(firedepartmentDto, firedepartment)

        return firedepartmentMapper.toFiredepartmentDto(firedepartmentRepository.save(firedepartment))
    }
}