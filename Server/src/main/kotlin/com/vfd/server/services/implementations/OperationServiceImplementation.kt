package com.vfd.server.services.implementations

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.mappers.AddressMapper
import com.vfd.server.mappers.OperationMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.AddressService
import com.vfd.server.services.OperationService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationServiceImplementation(
    private val operationRepository: OperationRepository,
    private val operationMapper: OperationMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val addressRepository: AddressRepository,
    private val addressMapper: AddressMapper,
    private val operationTypeRepository: OperationTypeRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository,
    private val addressService: AddressService,
) : OperationService {

    @Transactional
    override fun createOperation(
        emailAddress: String,
        operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartment = firefighter.requireFiredepartment()

        val operationType = operationTypeRepository.findByIdOrThrow(operationDto.operationType)

        val address = addressService.findOrCreateAddress(operationDto.address)

        val participants = firefighterRepository.findAllById(operationDto.participantsIds)

        participants.forEach { it.requireSameFiredepartment(firedepartment.firedepartmentId!!) }

        val operation = operationMapper.toOperationEntity(operationDto).apply {
            this.firedepartment = firedepartment
            this.operationType = operationType
            this.address = addressRepository.save(address)
            this.participants = participants.toMutableSet()
        }

        return operationMapper.toOperationDto(operationRepository.save(operation))
    }

    @Transactional(readOnly = true)
    override fun getOperations(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<OperationDtos.OperationResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            OPERATION_ALLOWED_SORTS,
            "operationDate,desc",
            200
        )

        return operationRepository
            .findAllByFiredepartmentFiredepartmentId(firedepartmentId, pageable)
            .map(operationMapper::toOperationDto)
            .toPageResponse()
    }

    @Transactional
    override fun updateOperation(
        emailAddress: String,
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val operation = operationRepository.findByIdOrThrow(operationId)

        operation.requireSameFiredepartment(firedepartmentId)

        operationDto.participantsIds?.let { ids ->
            val participants = firefighterRepository.findAllById(ids)
            operation.participants = participants.toMutableSet()
            operation.participants.forEach { it.requireSameFiredepartment(firedepartmentId) }
        }

        operation.participants.forEach { it.requireSameFiredepartment(firedepartmentId) }

        operationDto.address?.let { addressDto ->
            val address = addressService.findOrCreateAddress(addressDto)
            operation.address = address
        }

        operationMapper.patchOperation(operationDto, operation)

        operationDto.operationType
            ?.takeIf { it != operation.operationType?.operationType }
            ?.let { code ->
                val type = operationTypeRepository.findByIdOrThrow(code)
                operation.operationType = type
            }

        return operationMapper.toOperationDto(
            operationRepository.save(operation)
        )
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

        val operation = operationMapper.toOperationEntityDev(operationDto)

        val firedepartment = firedepartmentRepository.findByIdOrThrow(operationDto.firedepartmentId)
        operation.firedepartment = firedepartment

        val address = addressMapper.toAddressEntity(operationDto.address)
        operation.address = addressRepository.save(address)

        val operationType = operationTypeRepository.findByIdOrThrow(operationDto.operationType)
        operation.operationType = operationType

        operation.participants = firefighterRepository.findAllById(operationDto.participantIds).toMutableSet()

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

        val operation = operationRepository.findByIdOrThrow(operationId)

        return operationMapper.toOperationDto(operation)
    }

    @Transactional
    override fun updateOperationDev(
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse {

        val operation = operationRepository.findByIdOrThrow(operationId)

        operationDto.participantsIds?.let { ids ->
            val participants = firefighterRepository.findAllById(ids)
            operation.participants = participants.toMutableSet()
        }

        operationDto.address?.let { addressDto ->
            val address = addressService.findOrCreateAddress(addressDto)
            operation.address = address
        }

        operationMapper.patchOperation(operationDto, operation)

        operationDto.operationType
            ?.takeIf { it != operation.operationType?.operationType }
            ?.let { code ->
                val type = operationTypeRepository.findByIdOrThrow(code)
                operation.operationType = type
            }

        return operationMapper.toOperationDto(
            operationRepository.save(operation)
        )
    }
}