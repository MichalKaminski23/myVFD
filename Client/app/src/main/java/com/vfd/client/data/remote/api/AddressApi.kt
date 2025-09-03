package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AddressApi {

    @POST("/api/addresses")
    suspend fun createAddress(@Body addressDto: AddressDtos.AddressCreate): AddressDtos.AddressResponse

    @GET("/api/addresses")
    suspend fun getAllAddresses(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "addressId,asc"
    ): PageResponse<AddressDtos.AddressResponse>

    @GET("/api/addresses/{addressId}")
    suspend fun getAddressById(@Path("addressId") addressId: Int): AddressDtos.AddressResponse

//    @PATCH("/api/addresses/{addressId}")
//    suspend fun updateAddress(
//        @Path("addressId") addressId: Int,
//        @Body addressPatchDto: AddressDtos.AddressPatch
//    ): AddressDtos.AddressResponse
}