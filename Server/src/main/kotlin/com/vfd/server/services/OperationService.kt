package com.vfd.server.services

import com.vfd.server.dtos.OperationDtos
import com.vfd.server.shared.PageResponse

interface OperationService {

    fun createOperation(
        emailAddress: String,
        operationDto: OperationDtos.OperationCreate
    ): OperationDtos.OperationResponse

    fun getOperations(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationDate,desc",
        emailAddress: String
    ): PageResponse<OperationDtos.OperationResponse>

    fun updateOperation(
        emailAddress: String,
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse

    fun createOperationDev(operationDto: OperationDtos.OperationCreateDev): OperationDtos.OperationResponse

    fun getAllOperationsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationId,asc"
    ): PageResponse<OperationDtos.OperationResponse>

    fun getOperationByIdDev(operationId: Int): OperationDtos.OperationResponse

    fun updateOperationDev(
        operationId: Int,
        operationDto: OperationDtos.OperationPatch
    ): OperationDtos.OperationResponse
}