package com.vfd.server.mappers

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.entities.Asset
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [FiredepartmentMapper::class, AssetTypeMapper::class]
)
interface AssetMapper {

    fun toAssetDto(asset: Asset): AssetDtos.AssetResponse

    @Mapping(target = "assetId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    fun toAssetEntity(assetDto: AssetDtos.AssetCreate): Asset

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    fun patchAsset(assetDto: AssetDtos.AssetPatch, @MappingTarget asset: Asset)
}