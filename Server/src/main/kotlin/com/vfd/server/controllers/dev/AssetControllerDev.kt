package com.vfd.server.controllers.dev

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.services.AssetService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Assets", description = "CRUD for firedepartment assets. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/assets")
class AssetControllerDev(
    private val assetService: AssetService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAssetDev(@Valid @RequestBody assetDto: AssetDtos.AssetCreateDev): AssetDtos.AssetResponse =
        assetService.createAssetDev(assetDto)

    @GetMapping
    fun getAllAssetsDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "assetId,asc") sort: String
    ): PageResponse<AssetDtos.AssetResponse> =
        assetService.getAllAssetsDev(page, size, sort)

    @GetMapping("/{assetId}")
    fun getAssetByIdDev(@PathVariable assetId: Int): AssetDtos.AssetResponse =
        assetService.getAssetByIdDev(assetId)

    @PatchMapping("/{assetId}")
    fun updateAssetDev(
        @PathVariable assetId: Int,
        @Valid @RequestBody assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse =
        assetService.updateAssetDev(assetId, assetDto)
}