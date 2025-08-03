package com.vfd.server.mappers

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.entities.Firefighter
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface FirefighterMapper {

    fun toFirefighterDto(firefighter: Firefighter): FirefighterDtos.FirefighterResponse

    @Mapping(target = "firefighterId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    fun toFirefighterEntity(dto: FirefighterDtos.FirefighterCreate): Firefighter

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firedepartment", ignore = true)
    fun patchFirefighter(dto: FirefighterDtos.FirefighterPatch, @MappingTarget firefighter: Firefighter)
}