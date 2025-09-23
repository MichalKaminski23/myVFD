package com.vfd.server.controllers.dev

import com.vfd.server.dtos.FirefighterActivityDtos
import com.vfd.server.services.FirefighterActivityService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Firefighter Activities",
    description = "CRUD for firefighter activities (trainings, exams, etc.). (Development only)"
)
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/firefighter-activities")
class FirefighterActivityControllerDev(
    private val firefighterActivityService: FirefighterActivityService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createFirefighterActivityDev(
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityCreateDev
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.createFirefighterActivityDev(firefighterActivityDto)

    @GetMapping
    fun getAllFirefighterActivitiesDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "firefighterActivityId,asc") sort: String
    ): PageResponse<FirefighterActivityDtos.FirefighterActivityResponse> =
        firefighterActivityService.getAllFirefighterActivitiesDev(page, size, sort)

    @GetMapping("/{firefighterActivityId}")
    fun getFirefighterActivityByIdDev(
        @PathVariable firefighterActivityId: Int
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.getFirefighterActivityByIdDev(firefighterActivityId)

    @PatchMapping("/{firefighterActivityId}")
    fun updateFirefighterActivityDev(
        @PathVariable firefighterActivityId: Int,
        @Valid @RequestBody firefighterActivityDto: FirefighterActivityDtos.FirefighterActivityPatch
    ): FirefighterActivityDtos.FirefighterActivityResponse =
        firefighterActivityService.updateFirefighterActivityDev(firefighterActivityId, firefighterActivityDto)
}