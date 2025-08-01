package com.vfd.server.mappers

import com.vfd.server.dtos.FirefighterDto
import com.vfd.server.entities.Firefighter
import org.mapstruct.*

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface FirefighterMapper {

    @Mapping(source = "firedepartment.firedepartmentId", target = "firedepartmentId")
    fun fromFirefighterToFirefighterDto(firefighter: Firefighter): FirefighterDto

    @Mapping(target = "firefighterId", ignore = true)
    @Mapping(source = "firedepartmentId", target = "firedepartment.firedepartmentId")
    fun fromFirefighterDtoToFirefighter(firefighterDto: FirefighterDto): Firefighter

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun updateFirefighterFromFirefighterDto(firefighterDto: FirefighterDto, @MappingTarget firefighter: Firefighter)
}
