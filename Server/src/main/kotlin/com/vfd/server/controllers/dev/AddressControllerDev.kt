package com.vfd.server.controllers.dev

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.services.AddressService
import com.vfd.server.shared.PageResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(name = "Addresses", description = "CRUD for addresses. (Development only)")
@Profile("dev")
@Validated
@RestController
@RequestMapping("/api/dev/addresses")
class AddressControllerDev(
    private val addressService: AddressService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddressDev(@RequestBody addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse =
        addressService.createAddressDev(addressDto)

    @GetMapping
    fun getAllAddressesDev(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "addressId,asc") sort: String
    ): PageResponse<AddressDtos.AddressResponse> =
        addressService.getAllAddressesDev(page, size, sort)

    @GetMapping("/{addressId}")
    fun getAddressByIdDev(@PathVariable addressId: Int): AddressDtos.AddressResponse =
        addressService.getAddressByIdDev(addressId)
}