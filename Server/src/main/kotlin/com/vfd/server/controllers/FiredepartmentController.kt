package com.vfd.server.controllers

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.services.FiredepartmentService
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

@Tag(name = "Firedepartments", description = "CRUD for firedepartments.")
@Validated
@RestController
@RequestMapping("/api/firedepartments")
class FiredepartmentController(
    private val firedepartmentService: FiredepartmentService
) {

    @Operation(
        summary = "Create firedepartment",
        description = "Creates a new firedepartment and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Firedepartment created",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFiredepartment(
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.createFiredepartment(firedepartmentDto)

    @Operation(
        summary = "List firedepartments (paged)",
        description = """
            Returns a paginated list of firedepartments.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: firedepartmentId,asc) e.g. `name,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllFiredepartments(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firedepartmentId,asc") sort: String
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponse> =
        firedepartmentService.getAllFiredepartments(page, size, sort)

    @Operation(
        summary = "Get firedepartment by ID",
        description = "Returns a single firedepartment by `firedepartmentId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firedepartment found",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{firedepartmentId}")
    fun getFiredepartmentById(
        @PathVariable firedepartmentId: Int
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.getFiredepartmentById(firedepartmentId)

    @Operation(
        summary = "Update firedepartment",
        description = "Partially updates an existing firedepartment. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Firedepartment updated",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{firedepartmentId}")
    fun updateFiredepartment(
        @PathVariable firedepartmentId: Int,
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.updateFiredepartment(firedepartmentId, firedepartmentDto)
}