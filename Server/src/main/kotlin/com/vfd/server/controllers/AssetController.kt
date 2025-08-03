package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.services.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Assets", description = "Endpoints for managing fire department assets")
@RestController
@RequestMapping("/api/assets")
class AssetController(private val assetService: AssetService) {

    @Operation(summary = "Get all assets", description = "Returns a list of all registered assets.")
    @GetMapping
    fun getAllAssets(): List<AssetDtos.AssetResponse> =
        assetService.getAllAssets()

    @Operation(summary = "Get asset by ID", description = "Returns detailed information about an asset by its ID.")
    @GetMapping("/{id}")
    fun getAssetById(
        @Parameter(description = "ID of the asset to retrieve", example = "42")
        @PathVariable id: Int
    ): AssetDtos.AssetResponse =
        assetService.getAssetById(id)

    @Operation(summary = "Create a new asset", description = "Creates a new asset and assigns it to a fire department.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAsset(
        @Valid
        @RequestBody
        dto: AssetDtos.AssetCreate
    ): AssetDtos.AssetResponse =
        assetService.createAsset(dto)

    @Operation(summary = "Update asset fields", description = "Partially updates an existing asset by ID.")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateAsset(
        @Parameter(description = "ID of the asset to update", example = "42")
        @PathVariable id: Int,
        @Valid
        @RequestBody dto: AssetDtos.AssetPatch
    ) {
        assetService.updateAsset(id, dto)
    }

    @Operation(summary = "Delete asset by ID", description = "Removes an asset from the system.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAsset(
        @Parameter(description = "ID of the asset to delete", example = "42")
        @PathVariable id: Int
    ) {
        assetService.deleteAsset(id)
    }
}
