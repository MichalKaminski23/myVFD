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
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        summary = "Create a new asset for my firedepartment",
        description = "Creates a new asset associated with the firedepartment of the currently authenticated user and returns the created asset details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201", description = "Asset successfully created",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PostMapping("/my")
    @ResponseStatus(HttpStatus.CREATED)
    fun createAsset(
        @AuthenticationPrincipal principal: UserDetails,
        @Valid @RequestBody assetDto: AssetDtos.AssetCreate
    ): AssetDtos.AssetResponse =
        assetService.createAsset(principal.username, assetDto)

    @Operation(
        summary = "Get assets from my firedepartment",
        description = """
            Retrieves all assets associated with the firedepartment of the currently authenticated user.

            Query params:
            - `page` (default: 0)
            - `size` (default: 20)
            - `sort` (default: assetId,asc) e.g. `assetId,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Assets retrieved successfully",
                content = [Content(schema = Schema(implementation = AssetDtos.AssetResponse::class))]
            ),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @GetMapping("/my")
    fun getAssets(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "assetId,asc") sort: String,
        @AuthenticationPrincipal principal: UserDetails
    ): PageResponse<AssetDtos.AssetResponse> {
        return assetService.getAssets(page, size, sort, principal.username)
    }

    @Operation(
        summary = "Update asset from my firedepartment",
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
            ApiResponse(responseCode = "400", ref = "BadRequest"),
            ApiResponse(responseCode = "404", ref = "NotFound"),
            ApiResponse(responseCode = "403", ref = "Forbidden")
        ]
    )
    @PatchMapping("/my/{assetId}")
    fun updateAsset(
        @AuthenticationPrincipal principal: UserDetails,
        @PathVariable assetId: Int,
        @RequestBody assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse =
        assetService.updateAsset(principal.username, assetId, assetDto)
}