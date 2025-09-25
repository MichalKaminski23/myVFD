package com.vfd.server.mappers

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.entities.FirefighterActivity
import org.mapstruct.*

@Mapper(
    componentModel = "spring",
    uses = [FirefighterMapper::class, FirefighterActivityTypeMapper::class]
)
interface FirefighterActivityMapper {

    @Mapping(target = "firefighterId", source = "firefighter.firefighterId")
    @Mapping(target = "firefighterActivityTypeName", source = "firefighterActivityType.name")
    fun toFirefighterActivityDto(firefighterActivity: FirefighterActivity): FirefighterActivityDtos.FirefighterActivityResponse

    @Mapping(target = "firefighterActivityId", ignore = true)
    @Mapping(target = "firefighter", ignore = true)
    @Mapping(target = "firefighterActivityType", ignore = true)
    fun toFirefighterActivityEntity(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate): FirefighterActivity

    @Mapping(target = "firefighterActivityId", ignore = true)
    @Mapping(target = "firefighter", ignore = true)
    @Mapping(target = "firefighterActivityType", ignore = true)
    fun toFirefighterActivityEntityDev(firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreateDev): FirefighterActivity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firefighter", ignore = true)
    @Mapping(target = "firefighterActivityType", ignore = true)
    fun patchFirefighterActivity(
        firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch,
        @MappingTarget firefighterActivity: FirefighterActivity
    )
}