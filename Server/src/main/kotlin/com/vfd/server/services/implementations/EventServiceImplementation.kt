package com.vfd.server.services.implementations

import com.vfd.server.dtos.EventDtos
import com.vfd.server.entities.Event
import com.vfd.server.entities.Firedepartment
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.EventMapper
import com.vfd.server.repositories.EventRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.services.EventService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImplementation(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val firedepartmentRepository: FiredepartmentRepository
) : EventService {

    private val EVENT_ALLOWED_SORTS = setOf(
        "eventId",
        "header",
        "eventDate",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createEvent(eventDto: EventDtos.EventCreate): EventDtos.EventResponse {

        val firedepartment: Firedepartment = firedepartmentRepository.findById(eventDto.firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", eventDto.firedepartmentId) }

        val event: Event = eventMapper.toEventEntity(eventDto)

        event.firedepartment = firedepartment

        return eventMapper.toEventDto(eventRepository.save(event))
    }

    @Transactional(readOnly = true)
    override fun getAllEvents(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<EventDtos.EventResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = EVENT_ALLOWED_SORTS,
            defaultSort = "eventDate,desc",
            maxSize = 200
        )

        return eventRepository.findAll(pageable)
            .map(eventMapper::toEventDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getEventById(eventId: Int): EventDtos.EventResponse {

        val event = eventRepository.findById(eventId)
            .orElseThrow { ResourceNotFoundException("Event", "id", eventId) }

        return eventMapper.toEventDto(event)
    }

    @Transactional
    override fun updateEvent(eventId: Int, eventDto: EventDtos.EventPatch): EventDtos.EventResponse {

        val event = eventRepository.findById(eventId)
            .orElseThrow { ResourceNotFoundException("Event", "id", eventId) }

        eventMapper.patchEvent(eventDto, event)

        return eventMapper.toEventDto(eventRepository.save(event))
    }
}