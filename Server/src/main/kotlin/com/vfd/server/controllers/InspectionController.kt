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
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        summary = "Create a new inspection for my asset",
        description = "Creates a new inspection associated with the asset of the currently authenticated user and returns the created inspection details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Inspection successfully created",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createInspection(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse =
        inspectionService.createInspection(principal.username, inspectionDto)

    @Operation(
        summary = "List inspections from my assets",
        description = """
           Retrieves all inspections associated with the assets of the currently authenticated user.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: inspectionDate,desc) e.g. `inspectionDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Inspections retrieved successfully",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT')")
    @GetMapping("/my")
    fun getInspections(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "inspectionDate,desc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<InspectionDtos.InspectionResponse> =
        inspectionService.getInspections(page, size, sort, principal.username)

    @Operation(
        summary = "Update inspection from my asset",
        description = """
            Partially updates an existing inspection identified by `inspectionId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Inspection updated successfully",
                content = [Content(schema = Schema(implementation = InspectionDtos.InspectionResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @PatchMapping("/my/{inspectionId}")
    fun updateInspection(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable inspectionId: Int,
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse =
        inspectionService.updateInspection(principal.username, inspectionId, inspectionDto)
}