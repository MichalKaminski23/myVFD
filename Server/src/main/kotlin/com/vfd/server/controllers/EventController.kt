package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDtos
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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        summary = "Create event for my firedepartment",
        description = "Creates a new event associated with the firedepartment of the currently authenticated user and returns the created event details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Event successfully created",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createEvent(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody eventDto: EventDtos.EventCreate
    ): EventDtos.EventResponse =
        eventService.createEvent(principal.username, eventDto)

    @Operation(
        summary = "List events (paged)",
        description = """
            Retrieves all events associated with the firedepartment of the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: eventDate,desc) e.g. `eventDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Events retrieved successfully",
                content = [Content(schema = Schema(implementation = EventDtos.EventResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getEvents(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "eventDate,desc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<EventDtos.EventResponse> =
        eventService.getEvents(page, size, sort, principal.username)

    @Operation(
        summary = "Update event from my firedepartment",
        description = """
            Partially updates an existing event identified by `eventId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Event updated successfully",
                content = [Content(schema = Schema(implementation = EventDtos.EventResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/my/{eventId}")
    fun updateEvent(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable eventId: Int,
        @Valid @RequestBody eventPatchDto: EventDtos.EventPatch
    ): EventDtos.EventResponse =
        eventService.updateEvent(principal.username, eventId, eventPatchDto)
}