package com.vfd.server.controllers.dev

import com.vfd.server.dtos.InspectionDtos
import com.vfd.server.services.InspectionService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Inspections", description = "CRUD for inspections. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/inspections")
class InspectionControllerDev(
    private val inspectionService: InspectionService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInspectionDev(
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionCreate
    ): InspectionDtos.InspectionResponse =
        inspectionService.createInspectionDev(inspectionDto)

    @GetMapping
    fun getAllInspectionsDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "inspectionId,asc") sort: String
    ): PageResponse<InspectionDtos.InspectionResponse> =
        inspectionService.getAllInspectionsDev(page, size, sort)

    @GetMapping("/{inspectionId}")
    fun getInspectionByIdDev(
        @PathVariable inspectionId: Int
    ): InspectionDtos.InspectionResponse =
        inspectionService.getInspectionByIdDev(inspectionId)

    @PatchMapping("/{inspectionId}")
    fun updateInspectionDev(
        @PathVariable inspectionId: Int,
        @Valid @RequestBody inspectionDto: InspectionDtos.InspectionPatch
    ): InspectionDtos.InspectionResponse =
        inspectionService.updateInspectionDev(inspectionId, inspectionDto)
}