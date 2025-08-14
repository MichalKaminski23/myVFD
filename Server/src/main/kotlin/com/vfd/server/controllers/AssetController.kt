package com.vfd.server.controllers

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.services.AssetService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Assets", description = "CRUD for firedepartment assets.")
@Validated
@RestController
@RequestMapping("/api/assets")
class AssetController(
    private val assetService: AssetService
) {

    @Operation(
        summary = "Create asset",
        description = "Creates a new asset in a firedepartment and returns the created asset details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Asset successfully created",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request body", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAsset(@RequestBody assetDto: AssetDtos.AssetCreate): AssetDtos.AssetResponse =
        assetService.createAsset(assetDto)

    @Operation(
        summary = "List assets (paged)",
        description = """
            Returns a paginated list of all assets.
            
            **Query parameters:**
            - `page` (default: 0) — page number
            - `size` (default: 20) — number of elements per page
            - `sort` (default: assetId,asc) — sorting criteria, e.g. `name,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Assets list retrieved successfully",
                content = [Content(schema = Schema(implementation = Page::class))]
            ),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllAssets(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "assetId,asc") sort: String
    ): PageResponse<AssetDtos.AssetResponse> =
        assetService.getAllAssets(page, size, sort)

    @Operation(
        summary = "Get asset by ID",
        description = "Retrieves the details of a single asset specified by its `assetId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Asset found",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Asset not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{assetId}")
    fun getAssetById(@PathVariable assetId: Int): AssetDtos.AssetResponse =
        assetService.getAssetById(assetId)

    @Operation(
        summary = "Update asset",
        description = """
            Partially updates an existing asset identified by `assetId`.
            Only non-null fields in the request body will be updated.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Asset updated successfully",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request body", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Asset not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{assetId}")
    fun updateAsset(
        @PathVariable assetId: Int,
        @RequestBody assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse =
        assetService.updateAsset(assetId, assetDto)
}