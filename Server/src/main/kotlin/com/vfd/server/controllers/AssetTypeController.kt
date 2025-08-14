package com.vfd.server.controllers

import com.vfd.server.dtos.AssetTypeDtos
import com.vfd.server.services.AssetTypeService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Asset Types", description = "CRUD for asset types.")
@Validated
@RestController
@RequestMapping("/api/asset-types")
class AssetTypeController(
    private val assetTypeService: AssetTypeService
) {

    @Operation(
        summary = "Create asset type",
        description = "Creates a new asset type and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Asset type created",
                content = [Content(schema = Schema(implementation = AssetTypeDtos.AssetTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAssetType(
        @Valid @RequestBody assetTypeDto: AssetTypeDtos.AssetTypeCreate
    ): AssetTypeDtos.AssetTypeResponse =
        assetTypeService.createAssetType(assetTypeDto)

    @Operation(
        summary = "List asset types (paged)",
        description = """
            Returns a paginated list of asset types.
            
            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: assetType,asc) e.g. `name,desc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "OK", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllAssetTypes(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "assetType,asc") sort: String
    ): PageResponse<AssetTypeDtos.AssetTypeResponse> =
        assetTypeService.getAllAssetTypes(page, size, sort)

    @Operation(
        summary = "Get asset type by code",
        description = "Returns a single asset type by `assetTypeCode`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Asset type found",
                content = [Content(schema = Schema(implementation = AssetTypeDtos.AssetTypeResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{assetTypeCode}")
    fun getAssetTypeByCode(
        @PathVariable assetTypeCode: String
    ): AssetTypeDtos.AssetTypeResponse =
        assetTypeService.getAssetTypeByCode(assetTypeCode)

    @Operation(
        summary = "Update asset type",
        description = "Partially updates an existing asset type. Only non-null fields are applied."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Asset type updated",
                content = [Content(schema = Schema(implementation = AssetTypeDtos.AssetTypeResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Validation error", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{assetTypeCode}")
    fun updateAssetType(
        @PathVariable assetTypeCode: String,
        @Valid @RequestBody assetTypeDto: AssetTypeDtos.AssetTypePatch
    ): AssetTypeDtos.AssetTypeResponse =
        assetTypeService.updateAssetType(assetTypeCode, assetTypeDto)
}