package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.services.FirefighterService
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

@Tag(name = "Firefighters", description = "CRUD for firefighters.")
@Validated
@RestController
@RequestMapping("/api/firefighters")
class FirefighterController(
    private val firefighterService: FirefighterService
) {

    @Operation(
        summary = "Create firefighter",
        description = "Creates a new firefighter and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Firefighter created",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighter(
        @Valid @RequestBody firefighterCreateDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.createFirefighter(firefighterCreateDto)

    @Operation(
        summary = "List firefighters (paged)",
        description = """
            Returns a paginated list of firefighters.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: firefighterId,asc) e.g. `role,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllFirefighters(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterId,asc") sort: String
    ): PageResponse<FirefighterDtos.FirefighterResponse> =
        firefighterService.getAllFirefighters(page, size, sort)

    @Operation(
        summary = "Get firefighter by ID",
        description = "Returns a single firefighter by `firefighterId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter found",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{firefighterId}")
    fun getFirefighterById(
        @PathVariable firefighterId: Int
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.getFirefighterById(firefighterId)

    @Operation(
        summary = "Update firefighter (PATCH)",
        description = "Partially updates an existing firefighter. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter updated",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{firefighterId}")
    fun updateFirefighter(
        @PathVariable firefighterId: Int,
        @Valid @RequestBody firefighterPatchDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.updateFirefighter(firefighterId, firefighterPatchDto)

    @Operation(
        summary = "Get current firefighter",
        description = "Returns the currently authenticated firefighter based on the provided JWT token."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Current firefighter retrieved",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/me")
    fun getCurrentFirefighter(@AuthenticationPrincipal principal: UserDetails): FirefighterDtos.FirefighterResponse {
        return firefighterService.getFirefighterByEmailAddress(principal.username)
    }

    @Operation(
        summary = "Get pending firefighter applications for moderator",
        description = "Returns a list of pending firefighter applications for the moderator's fire department."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Pending applications retrieved",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "401", description = "Unauthorized", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/moderator/pending")
    fun getPendingFirefighters(@AuthenticationPrincipal principal: UserDetails): List<FirefighterDtos.FirefighterResponse> {
        return firefighterService.getPendingFirefighters(principal.username)
    }
}