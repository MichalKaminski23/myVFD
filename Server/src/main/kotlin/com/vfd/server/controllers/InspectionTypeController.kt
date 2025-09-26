package com.vfd.server.controllers

import com.vfd.server.dtos.InspectionTypeDtos
import com.vfd.server.services.InspectionTypeService
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

@Tag(name = "Inspection Types", description = "CRUD for inspection types.")
@Validated
@RestController
@RequestMapping("/api/inspection-types")
class InspectionTypeController(
    private val inspectionTypeService: InspectionTypeService
) {

    @Operation(
        summary = "Create inspection type",
        description = "Creates a new inspection type and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Inspection type created",
                content = [Content(schema = Schema(implementation = InspectionTypeDtos.InspectionTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInspectionType(
        @Valid @RequestBody inspectionTypeDto: InspectionTypeDtos.InspectionTypeCreate
    ): InspectionTypeDtos.InspectionTypeResponse =
        inspectionTypeService.createInspectionType(inspectionTypeDto)

    @Operation(
        summary = "Get all inspection types",
        description = """
            Retrieves all inspection types.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: inspectionType,asc) e.g. `inspectionType,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspection type found",
                content = [Content(schema = Schema(implementation = InspectionTypeDtos.InspectionTypeResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping
    fun getAllInspectionTypes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "inspectionType,asc") sort: String
    ): PageResponse<InspectionTypeDtos.InspectionTypeResponse> =
        inspectionTypeService.getAllInspectionTypes(page, size, sort)

    @Operation(
        summary = "Get inspection type by code",
        description = "Returns a single inspection type by `inspectionTypeCode`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspection type found",
                content = [Content(schema = Schema(implementation = InspectionTypeDtos.InspectionTypeResponse::class))]
            ),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/{inspectionTypeCode}")
    fun getInspectionTypeByCode(
        @PathVariable inspectionTypeCode: String
    ): InspectionTypeDtos.InspectionTypeResponse =
        inspectionTypeService.getInspectionTypeByCode(inspectionTypeCode)

    @Operation(
        summary = "Update inspection type",
        description = "Partially updates an existing inspection type. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspection type updated",
                content = [Content(schema = Schema(implementation = InspectionTypeDtos.InspectionTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/{inspectionTypeCode}")
    fun updateInspectionType(
        @PathVariable inspectionTypeCode: String,
        @Valid @RequestBody inspectionTypeDto: InspectionTypeDtos.InspectionTypePatch
    ): InspectionTypeDtos.InspectionTypeResponse =
        inspectionTypeService.updateInspectionType(inspectionTypeCode, inspectionTypeDto)
}