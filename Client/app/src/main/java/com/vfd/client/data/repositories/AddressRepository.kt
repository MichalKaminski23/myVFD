package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.AddressApi
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AddressRepository @Inject constructor(
    private val addressApi: AddressApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createAddress(addressDto: AddressDtos.AddressCreate): ApiResult<AddressDtos.AddressResponse> =
        safeApiCall { addressApi.createAddress(addressDto) }

    suspend fun getAllAddresses(
        page: Int = 0,
        size: Int = 20,
        sort: String = "addressId,asc"
    ): ApiResult<PageResponse<AddressDtos.AddressResponse>> =
        safeApiCall { addressApi.getAllAddresses(page, size, sort) }

    suspend fun getAddressById(addressId: Int): ApiResult<AddressDtos.AddressResponse> =
        safeApiCall { addressApi.getAddressById(addressId) }

//    suspend fun updateAddress(
//        addressId: Int,
//        addressDto: AddressDtos.AddressPatch
//    ): ApiResult<AddressDtos.AddressResponse> =
//        safeApiCall { addressApi.updateAddress(addressId, addressDto) }
}
