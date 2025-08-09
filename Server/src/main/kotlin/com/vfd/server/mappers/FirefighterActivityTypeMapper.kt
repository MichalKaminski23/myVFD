package com.vfd.server.mappers

import com.vfd.server.dtos.FirefighterActivityTypeDtos
import com.vfd.server.entities.FirefighterActivityType
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface FirefighterActivityTypeMapper {

    fun toFirefighterActivityTypeDto(firefighterActivityType: FirefighterActivityType): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse

    @Mapping(target = "firefighterActivityType", source = "firefighterActivityType")
    fun toFirefighterActivityTypeEntity(firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate): FirefighterActivityType

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchFirefighterActivityType(
        firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch,
        @MappingTarget firefighterActivityType: FirefighterActivityType
    )
}