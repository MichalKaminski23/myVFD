package com.vfd.server.services.implementations

import com.vfd.server.dtos.EventDtos
import com.vfd.server.mappers.EventMapper
import com.vfd.server.repositories.EventRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.services.EventService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventServiceImplementation(
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository
) : EventService {

    @Transactional
    override fun createEvent(emailAddress: String, eventDto: EventDtos.EventCreate): EventDtos.EventResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartment = firefighter.requireFiredepartment()

        val event = eventMapper.toEventEntity(eventDto).apply {
            this.firedepartment = firedepartment
        }

        return eventMapper.toEventDto(eventRepository.save(event))
    }

    @Transactional(readOnly = true)
    override fun getEvents(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<EventDtos.EventResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            sorts,
            "eventDate,asc",
            200
        )

        return eventRepository.findAllByFiredepartmentFiredepartmentId(firedepartmentId, pageable)
            .map(eventMapper::toEventDto).toPageResponse()
    }

    @Transactional
    override fun updateEvent(
        emailAddress: String,
        eventId: Int,
        eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val event = eventRepository.findByIdOrThrow(eventId)

        event.requireSameFiredepartment(firedepartmentId)

        eventMapper.patchEvent(eventDto, event)

        return eventMapper.toEventDto(eventRepository.save(event))
    }

    private val sorts = setOf(
        "eventId",
        "header",
        "eventDate",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createEventDev(eventDto: EventDtos.EventCreateDev): EventDtos.EventResponse {

        val firedepartment = firedepartmentRepository.findByIdOrThrow(eventDto.firedepartmentId)

        val event = eventMapper.toEventEntityDev(eventDto)

        event.firedepartment = firedepartment

        return eventMapper.toEventDto(eventRepository.save(event))
    }

    @Transactional(readOnly = true)
    override fun getAllEventsDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<EventDtos.EventResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "eventDate,desc",
            maxSize = 200
        )

        return eventRepository.findAll(pageable)
            .map(eventMapper::toEventDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getEventByIdDev(eventId: Int): EventDtos.EventResponse {

        val event = eventRepository.findByIdOrThrow(eventId)

        return eventMapper.toEventDto(event)
    }

    @Transactional
    override fun updateEventDev(eventId: Int, eventDto: EventDtos.EventPatch): EventDtos.EventResponse {

        val event = eventRepository.findByIdOrThrow(eventId)

        eventMapper.patchEvent(eventDto, event)

        return eventMapper.toEventDto(eventRepository.save(event))
    }
}