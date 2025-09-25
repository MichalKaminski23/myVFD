package com.vfd.server.mappers

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.entities.Firedepartment
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [AddressMapper::class]
)
interface FiredepartmentMapper {

    fun toFiredepartmentDto(firedepartment: Firedepartment): FiredepartmentDtos.FiredepartmentResponse

    fun toFiredepartmentDtoShort(firedepartment: Firedepartment): FiredepartmentDtos.FiredepartmentResponseShort

    @Mapping(target = "firedepartmentId", ignore = true)
    @Mapping(target = "address", ignore = true)
    fun toFiredepartmentEntity(firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate): Firedepartment

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", ignore = true)
    fun patchFiredepartment(
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch,
        @MappingTarget firedepartment: Firedepartment
    )
}