package com.vfd.server.controllers

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.services.InspectionService
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

@Tag(name = "Inspections", description = "CRUD for inspections.")
@Validated
@RestController
@RequestMapping("/api/inspections")
class InspectionController(
    private val inspectionService: InspectionService
) {

    @Operation(
        summary = "Create inspection",
        description = "Creates a new inspection and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Inspection created",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInspection(
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse =
        inspectionService.createInspection(inspectionDto)

    @Operation(
        summary = "List inspections (paged)",
        description = """
            Returns a paginated list of inspections.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: inspectionId,asc) e.g. `date,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllInspections(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "inspectionId,asc") sort: String
    ): PageResponse<InspectionDtos.InspectionResponse> =
        inspectionService.getAllInspections(page, size, sort)

    @Operation(
        summary = "Get inspection by ID",
        description = "Returns a single inspection by `inspectionId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspection found",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{inspectionId}")
    fun getInspectionById(
        @PathVariable inspectionId: Int
    ): InspectionDtos.InspectionResponse =
        inspectionService.getInspectionById(inspectionId)

    @Operation(
        summary = "Update inspection",
        description = "Partially updates an existing inspection. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspection updated",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{inspectionId}")
    fun updateInspection(
        @PathVariable inspectionId: Int,
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse =
        inspectionService.updateInspection(inspectionId, inspectionDto)
}