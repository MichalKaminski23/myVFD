package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterActivityTypeDtos
import com.vfd.server.services.FirefighterActivityTypeService
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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Firefighter Activity Types", description = "CRUD for firefighter activity types.")
@Validated
@RestController
@RequestMapping("/api/firefighter-activity-types")
class FirefighterActivityTypeController(
    private val firefighterActivityTypeService: FirefighterActivityTypeService
) {

    @Operation(
        summary = "Create firefighter activity type",
        description = "Creates a new firefighter activity type and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Firefighter activity type created",
                content = [Content(schema = Schema(implementation = FirefighterActivityTypeDtos.FirefighterActivityTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighterActivityType(
        @Valid @RequestBody firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypeCreate
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse =
        firefighterActivityTypeService.createFirefighterActivityType(firefighterActivityTypeDto)

    @Operation(
        summary = "Get all firefighter activity types",
        description = """
            Retrieves all firefighter activity types.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: firefighterActivityType,asc) e.g. `firefighterActivityType,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT', 'ADMIN')")
    @GetMapping
    fun getAllFirefighterActivityTypes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterActivityType,asc") sort: String
    ): PageResponse<FirefighterActivityTypeDtos.FirefighterActivityTypeResponse> =
        firefighterActivityTypeService.getAllFirefighterActivityTypes(page, size, sort)

    @Operation(
        summary = "Get firefighter activity type by code",
        description = "Returns a single firefighter activity type by `firefighterActivityTypeCode`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter activity type found",
                content = [Content(schema = Schema(implementation = FirefighterActivityTypeDtos.FirefighterActivityTypeResponse::class))]
            ),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{firefighterActivityTypeCode}")
    fun getFirefighterActivityTypeByCode(
        @PathVariable firefighterActivityTypeCode: String
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse =
        firefighterActivityTypeService.getFirefighterActivityTypeByCode(firefighterActivityTypeCode)

    @Operation(
        summary = "Update firefighter activity type",
        description = "Partially updates an existing firefighter activity type. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firefighter activity type updated",
                content = [Content(schema = Schema(implementation = FirefighterActivityTypeDtos.FirefighterActivityTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{firefighterActivityTypeCode}")
    fun updateFirefighterActivityType(
        @PathVariable firefighterActivityTypeCode: String,
        @Valid @RequestBody firefighterActivityTypeDto: FirefighterActivityTypeDtos.FirefighterActivityTypePatch
    ): FirefighterActivityTypeDtos.FirefighterActivityTypeResponse =
        firefighterActivityTypeService.updateFirefighterActivityType(
            firefighterActivityTypeCode,
            firefighterActivityTypeDto
        )
}