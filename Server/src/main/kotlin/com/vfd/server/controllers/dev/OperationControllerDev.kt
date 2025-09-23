package com.vfd.server.controllers.dev

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.services.OperationService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Operations", description = "CRUD for operations/interventions. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/operations")
class OperationControllerDev(
    private val operationService: OperationService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperationDev(
        @Valid @RequestBody operationDto: OperationDtos.OperationCreateDev
    ): OperationDtos.OperationResponse =
        operationService.createOperationDev(operationDto)

    @GetMapping
    fun getAllOperationsDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "operationId,asc") sort: String
    ): PageResponse<OperationDtos.OperationResponse> =
        operationService.getAllOperationsDev(page, size, sort)

    @GetMapping("/{operationId}")
    fun getOperationByIdDev(
        @PathVariable operationId: Int
    ): OperationDtos.OperationResponse =
        operationService.getOperationByIdDev(operationId)

    @PatchMapping("/{operationId}")
    fun updateOperationDev(
        @PathVariable operationId: Int,
        @Valid @RequestBody operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse =
        operationService.updateOperationDev(operationId, operationDto)
}