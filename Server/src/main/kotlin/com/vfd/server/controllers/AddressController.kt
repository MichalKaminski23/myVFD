package com.vfd.server.controllers

import com.vfd.server.dtos.AddressDtos
import com.vfd.server.services.AddressService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/addresses")
class AddressController(
    private val addressService: AddressService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAddress(
        @Valid @RequestBody dto: AddressDtos.AddressCreate
    ): AddressDtos.AddressResponse {
        return addressService.createAddress(dto)
    }

    @GetMapping
    fun getAllAddresses(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "addressId,asc") sort: String
    ): Page<AddressDtos.AddressResponse> {
        return addressService.getAllAddresses(page, size, sort)
    }

    @GetMapping("/{id}")
    fun getAddressById(@PathVariable id: Int): AddressDtos.AddressResponse {
        return addressService.getAddressById(id)
    }

    @PatchMapping("/{id}")
    fun patchAddress(
        @PathVariable id: Int,
        @Valid @RequestBody dto: AddressDtos.AddressPatch
    ): AddressDtos.AddressResponse {
        return addressService.updateAddress(id, dto)
    }
}
