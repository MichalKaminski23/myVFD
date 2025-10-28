package com.vfd.server.services.implementations

import com.vfd.server.dtos.OperationTypeDtos
import com.vfd.server.mappers.OperationTypeMapper
import com.vfd.server.repositories.OperationTypeRepository
import com.vfd.server.services.OperationTypeService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationTypeServiceImplementation(
    private val operationTypeRepository: OperationTypeRepository,
    private val operationTypeMapper: OperationTypeMapper
) : OperationTypeService {

    private val sorts = setOf("operationType", "name")

    @Transactional
    override fun createOperationType(
        operationTypeDto: OperationTypeDtos.OperationTypeCreate
    ): OperationTypeDtos.OperationTypeResponse {

        val operationType = operationTypeMapper.toOperationTypeEntity(operationTypeDto)

        operationTypeRepository.assertNotExistsByOperationType(operationTypeDto.operationType.uppercase())

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
            allowedFields = sorts,
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

        val operationType = operationTypeRepository.findByIdOrThrow(operationTypeCode)

        return operationTypeMapper.toOperationTypeDto(operationType)
    }

    @Transactional
    override fun updateOperationType(
        operationTypeCode: String,
        operationTypeDto: OperationTypeDtos.OperationTypePatch
    ): OperationTypeDtos.OperationTypeResponse {

        val operationType = operationTypeRepository.findByIdOrThrow(operationTypeCode)

        operationTypeMapper.patchOperationType(operationTypeDto, operationType)

        return operationTypeMapper.toOperationTypeDto(
            operationTypeRepository.save(operationType)
        )
    }
}