package com.vfd.server.services.implementations

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.entities.Operation
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.OperationMapper
import com.vfd.server.repositories.AddressRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.OperationRepository
import com.vfd.server.repositories.OperationTypeRepository
import com.vfd.server.services.OperationService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationServiceImplementation(
    private val operationRepository: OperationRepository,
    private val operationMapper: OperationMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper,
    private val operationTypeRepository: OperationTypeRepository
) : OperationService {

    override fun createOperation(
        emailAddress: String,
        operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse {
        TODO("Not yet implemented")
    }

    override fun getOperations(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<OperationDtos.OperationResponse> {
        TODO("Not yet implemented")
    }

    override fun updateOperation(
        emailAddress: String,
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse {
        TODO("Not yet implemented")
    }

    private val OPERATION_ALLOWED_SORTS = setOf(
        "operationId",
        "address.addressId",
        "operationType.operationType"
    )

    @Transactional
    override fun createOperationDev(
        operationDto: OperationDtos.OperationCreateDev
    ): OperationDtos.OperationResponse {

        val operation: Operation = operationMapper.toOperationEntityDev(operationDto)

        val firedepartment = firedepartmentRepository.findById(operationDto.firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", operationDto.firedepartmentId) }
        operation.firedepartment = firedepartment

        val address = addressMapper.toAddressEntity(operationDto.address)
        operation.address = addressRepository.save(address)

        val operationType = operationTypeRepository.findById(operationDto.operationType)
            .orElseThrow { ResourceNotFoundException("OperationType", "code", operationDto.operationType) }
        operation.operationType = operationType

        return operationMapper.toOperationDto(
            operationRepository.save(operation)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllOperationsDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<OperationDtos.OperationResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = OPERATION_ALLOWED_SORTS,
            defaultSort = "operationId,asc",
            maxSize = 200
        )

        return operationRepository.findAll(pageable)
            .map(operationMapper::toOperationDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getOperationByIdDev(
        operationId: Int
    ): OperationDtos.OperationResponse {

        val operation = operationRepository.findById(operationId)
            .orElseThrow { ResourceNotFoundException("Operation", "id", operationId) }

        return operationMapper.toOperationDto(operation)
    }

    @Transactional
    override fun updateOperationDev(
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse {

        val operation = operationRepository.findById(operationId)
            .orElseThrow { ResourceNotFoundException("Operation", "id", operationId) }

        operationMapper.patchOperation(operationDto, operation)

        operationDto.operationType
            ?.takeIf { it != operation.operationType?.operationType }
            ?.let { code ->
                val type = operationTypeRepository.findById(code)
                    .orElseThrow { ResourceNotFoundException("Operation's type", "code", code) }
                operation.operationType = type
            }

        return operationMapper.toOperationDto(
            operationRepository.save(operation)
        )
    }
}