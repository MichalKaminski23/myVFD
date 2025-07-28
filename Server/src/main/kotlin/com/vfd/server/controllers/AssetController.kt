package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDto
import com.vfd.server.services.AssetService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assets")
class AssetController(private val assetService: AssetService) {

    @GetMapping
    fun getAllAssets(): List<AssetDto> = assetService.getAllAssets()

    @GetMapping("/{id}")
    fun getAssetById(@PathVariable id: Int): AssetDto = assetService.getAssetById(id)

    @PostMapping
    fun createAsset(@RequestBody assetDto: AssetDto): AssetDto = assetService.createAsset(assetDto)

    @PatchMapping("/{id}")
    fun updateAsset(@PathVariable id: Int, @RequestBody assetDto: AssetDto) = assetService.updateAsset(id, assetDto)

    @DeleteMapping("/{id}")
    fun deleteAsset(@PathVariable id: Int) = assetService.deleteAsset(id)

}