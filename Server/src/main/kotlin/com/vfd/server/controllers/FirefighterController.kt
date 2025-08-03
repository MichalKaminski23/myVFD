package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.services.FirefighterService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Firefighters", description = "Endpoints for managing firefighters and their roles")
@RestController
@RequestMapping("/api/firefighters")
class FirefighterController(private val firefighterService: FirefighterService) {

    @Operation(summary = "Register a new firefighter")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighter(
        @Valid @RequestBody dto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse {
        return firefighterService.createFirefighter(dto)
    }

    @Operation(summary = "Get firefighter by ID")
    @GetMapping("/{id}")
    fun getFirefighterById(
        @Parameter(description = "ID of the firefighter to retrieve", example = "5")
        @PathVariable id: Int
    ): FirefighterDtos.FirefighterResponse {
        return firefighterService.getFirefighterById(id)
    }

    @Operation(summary = "Update firefighter data")
    @PatchMapping("/{id}")
    fun updateFirefighter(
        @Parameter(description = "ID of the firefighter to update", example = "5")
        @PathVariable id: Int,
        @Valid @RequestBody dto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse {
        return firefighterService.updateFirefighter(id, dto)
    }

    @Operation(summary = "Assign a new role to firefighter")
    @PatchMapping("/{id}/role/{roleName}")
    fun updateFirefighterRole(
        @Parameter(description = "ID of the firefighter", example = "5")
        @PathVariable id: Int,

        @Parameter(description = "New role name", example = "COMMANDER")
        @PathVariable roleName: String
    ): FirefighterDtos.FirefighterResponse {
        return firefighterService.updateFirefighterRole(id, roleName)
    }

    @Operation(summary = "Remove firefighter's role (set to USER)")
    @DeleteMapping("/{id}/role")
    fun removeRoleFromFirefighter(
        @Parameter(description = "ID of the firefighter", example = "5")
        @PathVariable id: Int
    ): FirefighterDtos.FirefighterResponse {
        return firefighterService.removeRoleFromFirefighter(id)
    }
}
