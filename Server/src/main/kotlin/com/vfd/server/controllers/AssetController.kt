package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.services.AssetService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assets")
class AssetController(
    private val assetService: AssetService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAsset(
        @Valid @RequestBody dto: AssetDtos.AssetCreate
    ): AssetDtos.AssetResponse =
        assetService.createAsset(dto)

    @GetMapping
    fun getAllAssets(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "assetId,asc") sort: String
    ): Page<AssetDtos.AssetResponse> =
        assetService.getAllAssets(page, size, sort)

    @GetMapping("/{id}")
    fun getAssetById(
        @PathVariable id: Int
    ): AssetDtos.AssetResponse =
        assetService.getAssetById(id)

    @PatchMapping("/{id}")
    fun patchAsset(
        @PathVariable id: Int,
        @Valid @RequestBody dto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse =
        assetService.updateAsset(id, dto) // PATCH semantyka z naszym DTO Patch
}
