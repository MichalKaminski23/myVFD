package com.vfd.server.controllers

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.services.OperationService
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

@Tag(name = "Operations", description = "CRUD for operations/interventions.")
@Validated
@RestController
@RequestMapping("/api/operations")
class OperationController(
    private val operationService: OperationService
) {

    @Operation(
        summary = "Create a new operation for my firedepartment",
        description = "Creates a new operation associated with the firedepartment of the currently authenticated user and returns the created operation details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Operation successfully created",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperation(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse =
        operationService.createOperation(principal.username, operationDto)

    @Operation(
        summary = "Get operations from my firedepartment",
        description = """
            Retrieves all operations associated with the firedepartment of the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: operationDate,desc) e.g. `operationDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Operations retrieved successfully",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasAnyRole('MEMBER', 'PRESIDENT')")
    @GetMapping("/my")
    fun getOperations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "operationDate,desc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<OperationDtos.OperationResponse> =
        operationService.getOperations(page, size, sort, principal.username)

    @Operation(
        summary = "Update operation from my firedepartment",
        description = """
            Partially updates an existing operation identified by `operationId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Operation updated successfully",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PreAuthorize("hasRole('PRESIDENT')")
    @PatchMapping("/my/{operationId}")
    fun updateOperation(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable operationId: Int,
        @Valid @RequestBody operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse =
        operationService.updateOperation(principal.username, operationId, operationDto)
}