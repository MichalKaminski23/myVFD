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
        summary = "Create a new firefighter from user",
        description = "Creates a new firefighter associated with the firedepartment and existing user and returns the created firefighter details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Firefighter successfully created",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighter(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.createFirefighter(principal.username, firefighterDto)

    @Operation(
        summary = "List firefighters from my firedepartment",
        description = """
            Retrieves all firefighters associated with the firedepartment of the currently authenticated user.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: user.firstName,asc) e.g. `firefighterRole,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighters retrieved successfully",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "Unauthorized"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getFirefighters(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "user.firstName,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<FirefighterDtos.FirefighterResponse> =
        firefighterService.getFirefighters(page, size, sort, principal.username)

    @Operation(
        summary = "Update firefighter from my firedepartment",
        description = """
            Partially updates an existing firefighter identified by `firefighterId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Firefighter updated successfully",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/my/{firefighterId}")
    fun updateFirefighter(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable firefighterId: Int,
        @Valid @RequestBody firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.updateFirefighter(principal.username, firefighterId, firefighterDto)

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
            ApiResponse(responseCode = "401", ref = "Unauthorized"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/me")
    fun getFirefighterByEmailAddress(@AuthenticationPrincipal principal: UserDetails): FirefighterDtos.FirefighterResponse {
        return firefighterService.getFirefighterByEmailAddress(principal.username)
    }

    @Operation(
        summary = "List pending firefighters from my firedepartment",
        description = """
            Retrieves all pending firefighters associated with the firedepartment of the currently authenticated user.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: user.firstName,asc) e.g. `firefighterRole,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Pending firefighters retrieved successfully",
                content = [Content(schema = Schema(implementation = FirefighterDtos.FirefighterResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "Unauthorized"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/pending")
    fun getPendingFirefighters(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "user.firstName,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<FirefighterDtos.FirefighterResponse> {
        return firefighterService.getPendingFirefighters(page, size, sort, principal.username)
    }
}