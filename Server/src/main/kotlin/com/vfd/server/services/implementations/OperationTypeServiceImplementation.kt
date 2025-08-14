package com.vfd.server.services.implementations

import com.vfd.server.dtos.OperationTypeDtos
import com.vfd.server.entities.OperationType
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.OperationTypeMapper
import com.vfd.server.repositories.OperationTypeRepository
import com.vfd.server.services.OperationTypeService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationTypeServiceImplementation(
    private val operationTypeRepository: OperationTypeRepository,
    private val operationTypeMapper: OperationTypeMapper
) : OperationTypeService {

    private val OPERATION_TYPE_ALLOWED_SORTS = setOf("operationType", "name")

    @Transactional
    override fun createOperationType(
        operationTypeDto: OperationTypeDtos.OperationTypeCreate
    ): OperationTypeDtos.OperationTypeResponse {

        val operationType: OperationType =
            operationTypeMapper.toOperationTypeEntity(operationTypeDto)

        return operationTypeMapper.toOperationTypeDto(
            operationTypeRepository.save(operationType)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllOperationTypes(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<OperationTypeDtos.OperationTypeResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = OPERATION_TYPE_ALLOWED_SORTS,
            defaultSort = "operationType,asc",
            maxSize = 200
        )

        return operationTypeRepository.findAll(pageable)
            .map(operationTypeMapper::toOperationTypeDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getOperationTypeByCode(
        operationTypeCode: String
    ): OperationTypeDtos.OperationTypeResponse {

        val operationType = operationTypeRepository.findById(operationTypeCode)
            .orElseThrow { ResourceNotFoundException("Operation's type", "code", operationTypeCode) }

        return operationTypeMapper.toOperationTypeDto(operationType)
    }

    @Transactional
    override fun updateOperationType(
        operationTypeCode: String,
        operationTypeDto: OperationTypeDtos.OperationTypePatch
    ): OperationTypeDtos.OperationTypeResponse {

        val operationType = operationTypeRepository.findById(operationTypeCode)
            .orElseThrow { ResourceNotFoundException("Operation's type", "code", operationTypeCode) }

        operationTypeMapper.patchOperationType(operationTypeDto, operationType)

        return operationTypeMapper.toOperationTypeDto(
            operationTypeRepository.save(operationType)
        )
    }
}