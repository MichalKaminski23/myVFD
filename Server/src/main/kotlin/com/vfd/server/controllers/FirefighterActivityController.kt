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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        summary = "Create firefighter's activity for me",
        description = "Creates a new firefighter's associated with the currently authenticated user and returns the created firefighter's details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Firefighter's activity successfully created",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT')")
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighterActivity(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreate
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.createFirefighterActivity(principal.username, firefighterActivityDto)

    @Operation(
        summary = "Get my firefighter's activities",
        description = """
            Retrieves all firefighter's activities associated with the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: activityDate,asc) e.g. `activityDate,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Activities retrieved successfully",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT')")
    @GetMapping("/my")
    fun getFirefighterActivities(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "activityDate,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> =
        firefighterActivityService.getFirefighterActivities(page, size, sort, principal.username)

    @Operation(
        summary = "Get my firedepartment's firefighters activities",
        description = """
            Retrieves all firefighters activities associated with the firedepartment of the currently authenticated user.  
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: activityDate,asc) e.g. `activityDate,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Activities retrieved successfully",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @GetMapping("/my/firefighters")
    fun getFirefightersActivities(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "activityDate,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> =
        firefighterActivityService.getFirefightersActivities(page, size, sort, principal.username)

    @Operation(
        summary = "List pending activities from my firedepartment's firefighters",
        description = """
            Retrieves all pending activities associated with the firedepartment of the currently authenticated user.
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: activityDate,asc) e.g. `activityDate,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Pending activities retrieved successfully",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "Unauthorized"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @GetMapping("/my/firefighters/pending")
    fun getPendingFirefightersActivities(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "activityDate,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> =
        firefighterActivityService.getPendingFirefightersActivities(page, size, sort, principal.username)

    @Operation(
        summary = "Update my firefighter's activity",
        description = """
            Partially updates an existing firefighter's activity identified by `firefighterActivityId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Firefighter's activity updated successfully",
                content = [Content(schema = Schema(implementation = FirefighterActivityDtos.FirefighterActivityResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT')")
    @PatchMapping("/my/{firefighterActivityId}")
    fun updateFirefighterActivity(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable firefighterActivityId: Int,
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.updateFirefighterActivity(
            principal.username,
            firefighterActivityId,
            firefighterActivityDto
        )
}