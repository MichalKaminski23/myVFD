package com.vfd.server.services

import com.vfd.server.dtos.OperationDtos
import org.springframework.data.domain.Page

interface OperationService {

    fun createOperation(operationDto: OperationDtos.OperationCreate): OperationDtos.OperationResponse

    fun getAllOperations(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationId,asc"
    ): Page<OperationDtos.OperationResponse>

    fun getOperationById(operationId: Int): OperationDtos.OperationResponse

    fun updateOperation(operationId: Int, operationDto: OperationDtos.OperationPatch): OperationDtos.OperationResponse
}