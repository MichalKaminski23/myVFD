package com.vfd.server.controllers

import com.vfd.server.dtos.EventDtos
import com.vfd.server.services.EventService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Events", description = "CRUD for events (blog-style posts/announcements).")
@Validated
@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {

    @Operation(
        summary = "Create event",
        description = "Creates a new event and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Event created",
                content = [Content(schema = Schema(implementation = EventDtos.EventResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(
        @Valid @RequestBody eventCreateDto: EventDtos.EventCreate
    ): EventDtos.EventResponse =
        eventService.createEvent(eventCreateDto)

    @Operation(
        summary = "List events (paged)",
        description = """
            Returns a paginated list of events.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: eventDate,desc) e.g. `header,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllEvents(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "eventDate,desc") sort: String
    ): PageResponse<EventDtos.EventResponse> =
        eventService.getAllEvents(page, size, sort)

    @Operation(
        summary = "Get event by ID",
        description = "Returns a single event by `eventId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Event found",
                content = [Content(schema = Schema(implementation = EventDtos.EventResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{eventId}")
    fun getEventById(
        @PathVariable eventId: Int
    ): EventDtos.EventResponse =
        eventService.getEventById(eventId)

    @Operation(
        summary = "Update event (PATCH)",
        description = "Partially updates an existing event. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Event updated",
                content = [Content(schema = Schema(implementation = EventDtos.EventResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{eventId}")
    fun updateEvent(
        @PathVariable eventId: Int,
        @Valid @RequestBody eventPatchDto: EventDtos.EventPatch
    ): EventDtos.EventResponse =
        eventService.updateEvent(eventId, eventPatchDto)
}