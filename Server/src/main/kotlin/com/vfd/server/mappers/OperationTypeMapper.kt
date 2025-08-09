package com.vfd.server.mappers

import com.vfd.server.dtos.OperationTypeDtos
import com.vfd.server.entities.OperationType
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface OperationTypeMapper {

    fun toOperationTypeDto(operationType: OperationType): OperationTypeDtos.OperationTypeResponse

    @Mapping(target = "operationType", source = "operationType")
    fun toOperationTypeEntity(operationTypeDto: OperationTypeDtos.OperationTypeCreate): OperationType

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchOperationType(
        operationTypeDto: OperationTypeDtos.OperationTypePatch,
        @MappingTarget operationType: OperationType
    )
}