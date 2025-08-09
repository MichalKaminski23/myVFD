package com.vfd.server.mappers

import com.vfd.server.dtos.AssetTypeDtos
import com.vfd.server.entities.AssetType
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface AssetTypeMapper {

    fun toAssetTypeDto(assetType: AssetType): AssetTypeDtos.AssetTypeResponse

    @Mapping(target = "assetType", source = "assetType")
    fun toAssetTypeEntity(assetTypeDto: AssetTypeDtos.AssetTypeCreate): AssetType

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchAssetType(assetTypeDto: AssetTypeDtos.AssetTypePatch, @MappingTarget assetType: AssetType)
}