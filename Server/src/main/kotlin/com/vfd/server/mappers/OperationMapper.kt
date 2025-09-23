package com.vfd.server.mappers

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.entities.Operation
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [FiredepartmentMapper::class, AddressMapper::class, OperationTypeMapper::class, FirefighterMapper::class]
)
interface OperationMapper {

    @Mapping(target = "operationTypeName", source = "operationType.name")
    fun toOperationDto(operation: Operation): OperationDtos.OperationResponse

    @Mapping(target = "operationId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "operationType", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "participants", ignore = true)
    fun toOperationEntity(operationDto: OperationDtos.OperationCreate): Operation

    @Mapping(target = "operationId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "operationType", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "participants", ignore = true)
    fun toOperationEntityDev(operationDto: OperationDtos.OperationCreateDev): Operation

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "operationType", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "participants", ignore = true)
    fun patchOperation(operationDto: OperationDtos.OperationPatch, @MappingTarget operation: Operation)
}