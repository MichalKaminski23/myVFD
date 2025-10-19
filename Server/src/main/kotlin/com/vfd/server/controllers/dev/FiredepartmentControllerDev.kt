package com.vfd.server.controllers.dev

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.services.FiredepartmentService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Firedepartments", description = "CRUD for firedepartments. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/firedepartments")
class FiredepartmentControllerDev(
    private val firedepartmentService: FiredepartmentService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFiredepartmentDev(
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.createFiredepartmentDev(firedepartmentDto)

    @GetMapping("/{firedepartmentId}")
    fun getFiredepartmentByIdDev(
        @PathVariable firedepartmentId: Int
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.getFiredepartmentByIdDev(firedepartmentId)

    @PatchMapping("/{firedepartmentId}")
    fun updateFiredepartmentDev(
        @PathVariable firedepartmentId: Int,
        @Valid @RequestBody firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse =
        firedepartmentService.updateFiredepartmentDev(firedepartmentId, firedepartmentDto)
}