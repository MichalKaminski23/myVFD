package com.vfd.server.mappers

import com.vfd.server.dtos.AssetDto
import com.vfd.server.entities.Asset
import org.mapstruct.*

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface AssetMapper {

    @Mapping(source = "firedepartment.firedepartmentId", target = "firedepartmentId")
    @Mapping(source = "assetType.assetType", target = "assetType")
    fun fromAssetToAssetDto(asset: Asset): AssetDto

    @Mapping(target = "assetId", ignore = true)
    @Mapping(source = "firedepartmentId", target = "firedepartment.firedepartmentId")
    @Mapping(source = "assetType", target = "assetType.assetType")
    fun fromAssetDtoToAsset(assetDto: AssetDto): Asset

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    fun updateAssetFromAssetDto(assetDto: AssetDto, @MappingTarget asset: Asset)
}