package com.vfd.server.mappers

import com.vfd.server.dtos.InspectionTypeDtos
import com.vfd.server.entities.InspectionType
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface InspectionTypeMapper {

    fun toInspectionTypeDto(inspectionType: InspectionType): InspectionTypeDtos.InspectionTypeResponse

    @Mapping(target = "inspectionType", source = "inspectionType")
    fun toInspectionTypeEntity(inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate): InspectionType

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchInspectionType(
        inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch,
        @MappingTarget inspectionType: InspectionType
    )
}