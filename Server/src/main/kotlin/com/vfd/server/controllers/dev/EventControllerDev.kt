package com.vfd.server.controllers.dev

import com.vfd.server.dtos.EventDtos
import com.vfd.server.services.EventService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Events", description = "CRUD for events (blog-style posts/announcements). (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/events")
class EventControllerDev(
    private val eventService: EventService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEventDev(
        @Valid @RequestBody eventDto: EventDtos.EventCreateDev
    ): EventDtos.EventResponse =
        eventService.createEventDev(eventDto)

    @GetMapping
    fun getAllEventsDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "eventDate,desc") sort: String
    ): PageResponse<EventDtos.EventResponse> =
        eventService.getAllEventsDev(page, size, sort)

    @GetMapping("/{eventId}")
    fun getEventByIdDev(
        @PathVariable eventId: Int
    ): EventDtos.EventResponse =
        eventService.getEventByIdDev(eventId)

    @PatchMapping("/{eventId}")
    fun updateEventDev(
        @PathVariable eventId: Int,
        @Valid @RequestBody eventDto: EventDtos.EventPatch
    ): EventDtos.EventResponse =
        eventService.updateEventDev(eventId, eventDto)
}