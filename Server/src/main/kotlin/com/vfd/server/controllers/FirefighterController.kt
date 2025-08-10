package com.vfd.server.controllers

import com.vfd.server.dtos.FirefighterDtos
import com.vfd.server.services.FirefighterService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/firefighters")
class FirefighterController(
    private val firefighterService: FirefighterService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighter(
        @Valid @RequestBody dto: FirefighterDtos.FirefighterCreate
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.createFirefighter(dto)

    @GetMapping
    fun getAllFirefighters(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterId,asc") sort: String
    ): Page<FirefighterDtos.FirefighterResponse> =
        firefighterService.getAllFirefighters(page, size, sort)

    @GetMapping("/{id}")
    fun getFirefighterById(@PathVariable id: Int): FirefighterDtos.FirefighterResponse =
        firefighterService.getFirefighterById(id)

    @PatchMapping("/{id}")
    fun patchFirefighter(
        @PathVariable id: Int,
        @Valid @RequestBody dto: FirefighterDtos.FirefighterPatch
    ): FirefighterDtos.FirefighterResponse =
        firefighterService.updateFirefighter(id, dto)
}
