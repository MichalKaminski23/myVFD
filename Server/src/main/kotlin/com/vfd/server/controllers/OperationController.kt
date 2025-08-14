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
        summary = "Create operation",
        description = "Creates a new operation (intervention) and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Operation created",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperation(
        @Valid @RequestBody operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse =
        operationService.createOperation(operationDto)

    @Operation(
        summary = "List operations (paged)",
        description = """
            Returns a paginated list of operations.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: operationId,desc) e.g. `operationDate,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllOperations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "operationId,desc") sort: String
    ): PageResponse<OperationDtos.OperationResponse> =
        operationService.getAllOperations(page, size, sort)

    @Operation(
        summary = "Get operation by ID",
        description = "Returns a single operation by `operationId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Operation found",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{operationId}")
    fun getOperationById(
        @PathVariable operationId: Int
    ): OperationDtos.OperationResponse =
        operationService.getOperationById(operationId)

    @Operation(
        summary = "Update operation",
        description = "Partially updates an existing operation. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Operation updated",
                content = [Content(schema = Schema(implementation = OperationDtos.OperationResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{operationId}")
    fun updateOperation(
        @PathVariable operationId: Int,
        @Valid @RequestBody operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse =
        operationService.updateOperation(operationId, operationDto)
}