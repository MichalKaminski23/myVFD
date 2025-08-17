package com.vfd.server.mappers

import com.vfd.server.dtos.EventDtos
import com.vfd.server.entities.Event
import org.mapstruct.*

@Mapper(
    componentModel = "spring"
)
interface EventMapper {

    @Mapping(source = "firedepartment.firedepartmentId", target = "firedepartmentId")
    fun toEventDto(event: Event): EventDtos.EventResponse

    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "firedepartment", ignore = true)
    fun toEventEntity(eventDto: EventDtos.EventCreate): Event

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun patchEvent(eventDto: EventDtos.EventPatch, @MappingTarget event: Event)
}