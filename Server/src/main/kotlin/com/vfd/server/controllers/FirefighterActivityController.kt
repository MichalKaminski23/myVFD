package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.services.FirefighterActivityService
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

@Tag(name = "Firefighter Activities", description = "CRUD for firefighter activities (trainings, exams, etc.).")
@Validated
@RestController
@RequestMapping("/api/firefighter-activities")
class FirefighterActivityController(
    private val firefighterActivityService: FirefighterActivityService
) {

    @Operation(
        summary = "Create firefighter activity",
        description = "Creates a new firefighter activity and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Firefighter activity created",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighterActivity(
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.createFirefighterActivity(firefighterActivityDto)

    @Operation(
        summary = "List firefighter activities (paged)",
        description = """
            Returns a paginated list of firefighter activities.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: firefighterActivityId,asc) e.g. `activityDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllFirefighterActivities(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterActivityId,asc") sort: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> =
        firefighterActivityService.getAllFirefighterActivities(page, size, sort)

    @Operation(
        summary = "Get firefighter activity by ID",
        description = "Returns a single firefighter activity by `firefighterActivityId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter activity found",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{firefighterActivityId}")
    fun getFirefighterActivityById(
        @PathVariable firefighterActivityId: Int
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.getFirefighterActivityById(firefighterActivityId)

    @Operation(
        summary = "Update firefighter activity",
        description = "Partially updates an existing firefighter activity. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter activity updated",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{firefighterActivityId}")
    fun updateFirefighterActivity(
        @PathVariable firefighterActivityId: Int,
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.updateFirefighterActivity(firefighterActivityId, firefighterActivityDto)
}