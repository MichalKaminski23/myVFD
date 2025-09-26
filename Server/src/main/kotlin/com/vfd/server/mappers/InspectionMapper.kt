package com.vfd.server.mappers

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.entities.Inspection
import org.mapstruct.*

@Mapper(componentModel = "spring", uses = [AssetMapper::class, InspectionTypeMapper::class])
interface InspectionMapper {

    @Mapping(target = "assetId", source = "asset.assetId")
    @Mapping(target = "inspectionTypeName", source = "inspectionType.name")
    fun toInspectionDto(inspection: Inspection): InspectionDtos.InspectionResponse

    @Mapping(target = "inspectionId", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "inspectionType", ignore = true)
    fun toInspectionEntity(inspectionDto: InspectionDtos.InspectionCreate): Inspection

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "inspectionType", ignore = true)
    fun patchInspection(inspectionDto: InspectionDtos.InspectionPatch, @MappingTarget inspection: Inspection)
}