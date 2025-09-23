package com.vfd.server.services

import com.vfd.server.dtos.FiredepartmentDtos
import com.vfd.server.shared.PageResponse

interface FiredepartmentService {

    fun createFiredepartment(
        emailAddress: String,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate
    ): FiredepartmentDtos.FiredepartmentResponse

    fun getFiredepartmentsShort(
        page: Int = 0,
        size: Int = 20,
        sort: String = "name,asc"
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponseShort>

    fun getFiredepartment(emailAddress: String): FiredepartmentDtos.FiredepartmentResponse

    fun updateFiredepartment(
        emailAddress: String,
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse

    fun createFiredepartmentDev(firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate): FiredepartmentDtos.FiredepartmentResponse

    fun getAllFiredepartmentsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firedepartmentId,asc"
    ): PageResponse<FiredepartmentDtos.FiredepartmentResponse>

    fun getFiredepartmentByIdDev(firedepartmentId: Int): FiredepartmentDtos.FiredepartmentResponse

    fun updateFiredepartmentDev(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse
}