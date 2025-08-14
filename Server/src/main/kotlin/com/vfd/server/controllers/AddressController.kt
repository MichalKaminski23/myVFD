package com.vfd.server.controllers

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.services.AddressService
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

@Tag(name = "Addresses", description = "CRUD for addresses.")
@Validated
@RestController
@RequestMapping("/api/addresses")
class AddressController(
    private val addressService: AddressService
) {

    @Operation(
        summary = "Create address",
        description = "Creates a new address and returns its details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Address successfully created",
                content = [Content(schema = Schema(implementation = AddressDtos.AddressResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request body", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(@RequestBody addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse =
        addressService.createAddress(addressDto)

    @Operation(
        summary = "List addresses (paged)",
        description = """
            Returns a paginated list of addresses.
            
            **Query parameters:**
            - `page` (default: 0) — page number
            - `size` (default: 20) — items per page
            - `sort` (default: addressId,asc) — sorting, e.g. `city,asc`
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Addresses list retrieved successfully",
                content = [Content(schema = Schema(implementation = Page::class))]
            ),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping
    fun getAllAddresses(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "addressId,asc") sort: String
    ): PageResponse<AddressDtos.AddressResponse> =
        addressService.getAllAddresses(page, size, sort)

    @Operation(
        summary = "Get address by ID",
        description = "Retrieves a single address by its `addressId`."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Address found",
                content = [Content(schema = Schema(implementation = AddressDtos.AddressResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Address not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @GetMapping("/{addressId}")
    fun getAddressById(@PathVariable addressId: Int): AddressDtos.AddressResponse =
        addressService.getAddressById(addressId)

    @Operation(
        summary = "Update address",
        description = """
            Partially updates an address identified by `addressId`.
            Only non-null fields in the request body will be applied.
        """
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Address updated successfully",
                content = [Content(schema = Schema(implementation = AddressDtos.AddressResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Invalid request body", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Address not found", content = [Content()]),
            ApiResponse(responseCode = "403", description = "Forbidden", content = [Content()])
        ]
    )
    @PatchMapping("/{addressId}")
    fun updateAddress(
        @PathVariable addressId: Int,
        @RequestBody addressDto: AddressDtos.AddressPatch
    ): AddressDtos.AddressResponse =
        addressService.updateAddress(addressId, addressDto)
}