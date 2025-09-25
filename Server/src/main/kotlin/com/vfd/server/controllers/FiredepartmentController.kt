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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        summary = "Create a new firedepartment",
        description = "Creates a new firedepartment and returns the created firedepartment details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Firedepartment successfully created",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden"),
            ApiResponse(responseCode = "409", ref = "Conflict")
        ]
    )
    @PostMapping("/admin")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFiredepartment(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.createFiredepartment(principal.username, firedepartmentDto)

    @Operation(
        summary = "Get firedepartments short version",
        description = """
            Retrieves all firedepartments.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: name,asc) e.g. `name,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Firedepartments retrieved successfully",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            )
        ]
    )
    @GetMapping
    fun getFiredepartmentsShort(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "name,asc") sort: String
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponseShort> =
        firedepartmentService.getFiredepartmentsShort(page, size, sort)

    @Operation(
        summary = "Get my firedepartment",
        description =
            "Retrieves firedepartment of the currently authenticated user."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Firedepartment retrieved successfully",
                content = [Content(schema = Schema(implementation = FiredepartmentDtos.FiredepartmentResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getFiredepartment(
        @AuthenticationPrincipal principal: UserDetails
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.getFiredepartment(principal.username)

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
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/admin/{firedepartmentId}")
    //@PreAuthorize("hasRole('ADMIN')")
    fun updateFiredepartment(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable firedepartmentId: Int,
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.updateFiredepartment(principal.username, firedepartmentId, firedepartmentDto)
}