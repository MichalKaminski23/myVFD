package com.vfd.server.controllers

import com.vfd.server.dtos.OperationTypeDtos
import com.vfd.server.services.OperationTypeService
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

@Tag(name = "Operation Types", description = "CRUD for operation types (dictionary).")
@Validated
@RestController
@RequestMapping("/api/operation-types")
class OperationTypeController(
    private val operationTypeService: OperationTypeService
) {

    @Operation(
        summary = "Create operation type",
        description = "Creates a new operation type and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Operation type created",
                content = [Content(schema = Schema(implementation = OperationTypeDtos.OperationTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperationType(
        @Valid @RequestBody operationTypeDto: OperationTypeDtos.OperationTypeCreate
    ): OperationTypeDtos.OperationTypeResponse =
        operationTypeService.createOperationType(operationTypeDto)

    @Operation(
        summary = "Get all operation types",
        description = """
            Retrieves all operation types.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: operationType,asc) e.g. `operationType,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operation types retrieved successfully"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping
    fun getAllOperationTypes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "operationTypeCode,asc") sort: String
    ): PageResponse<OperationTypeDtos.OperationTypeResponse> =
        operationTypeService.getAllOperationTypes(page, size, sort)

    @Operation(
        summary = "Get operation type by code",
        description = "Returns a single operation type entry by `operationTypeCode`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Operation type found",
                content = [Content(schema = Schema(implementation = OperationTypeDtos.OperationTypeResponse::class))]
            ),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/{operationTypeCode}")
    fun getOperationTypeByCode(
        @PathVariable operationTypeCode: String
    ): OperationTypeDtos.OperationTypeResponse =
        operationTypeService.getOperationTypeByCode(operationTypeCode)

    @Operation(
        summary = "Update operation type",
        description = "Partially updates an operation type. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Operation type updated",
                content = [Content(schema = Schema(implementation = OperationTypeDtos.OperationTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/{operationTypeCode}")
    fun updateOperationType(
        @PathVariable operationTypeCode: String,
        @Valid @RequestBody operationTypeDto: OperationTypeDtos.OperationTypePatch
    ): OperationTypeDtos.OperationTypeResponse =
        operationTypeService.updateOperationType(operationTypeCode, operationTypeDto)
}