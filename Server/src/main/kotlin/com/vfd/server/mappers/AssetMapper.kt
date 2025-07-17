package com.vfd.server.mappers

import com.vfd.server.dtos.AssetDto
import com.vfd.server.entities.Asset
import org.mapstruct.*

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface AssetMapper {

    // encja → DTO: wyciągamy tylko proste pola
    @Mapping(source = "firedepartment.firedepartmentId", target = "firedepartmentId")
    @Mapping(source = "assetType.assetType", target = "assetType")
    fun toDto(asset: Asset): AssetDto

    // DTO → encja: ignorujemy relacje, żeby nie próbował mapować String → AssetType itp.
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    fun toEntity(dto: AssetDto): Asset

    // częściowa aktualizacja: też pomijamy relacje
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firedepartment", ignore = true)
    @Mapping(target = "assetType", ignore = true)
    fun updateEntityFromDto(dto: AssetDto, @MappingTarget asset: Asset)
}