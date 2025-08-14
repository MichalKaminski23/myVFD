package com.vfd.server.services

import com.vfd.server.dtos.OperationTypeDtos
import org.springframework.data.domain.Page

interface OperationTypeService {

    fun createOperationType(operationTypeDto: OperationTypeDtos.OperationTypeCreate): OperationTypeDtos.OperationTypeResponse

    fun getAllOperationTypes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "operationType,asc"
    ): Page<OperationTypeDtos.OperationTypeResponse>

    fun getOperationTypeByCode(operationTypeCode: String): OperationTypeDtos.OperationTypeResponse

    fun updateOperationType(
        operationTypeCode: String,
        operationTypeDto: OperationTypeDtos.OperationTypePatch
    ): OperationTypeDtos.OperationTypeResponse
}