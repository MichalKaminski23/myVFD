package com.vfd.server.controllers.dev

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.services.FirefighterService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Firefighters", description = "CRUD for firefighters. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/firefighters")
class FirefighterControllerDev(
    private val firefighterService: FirefighterService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighterDev(
        @Valid @RequestBody firefighterDto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.createFirefighterDev(firefighterDto)

    @GetMapping
    fun getAllFirefightersDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterId,asc") sort: String,
    ): PageResponse<FirefighterDtos.FirefighterResponse> =
        firefighterService.getAllFirefightersDev(page, size, sort)

    @GetMapping("/{firefighterId}")
    fun getFirefighterByIdDev(
        @PathVariable firefighterId: Int
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.getFirefighterByIdDev(firefighterId)

    @PatchMapping("/{firefighterId}")
    fun updateFirefighterDev(
        @PathVariable firefighterId: Int,
        @Valid @RequestBody firefighterDto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.updateFirefighterDev(firefighterId, firefighterDto)
}