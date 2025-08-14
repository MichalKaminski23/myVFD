package com.vfd.server.services

import com.vfd.server.dtos.FiredepartmentDtos
import org.springframework.data.domain.Page

interface FiredepartmentService {

    fun createFiredepartment(firedepartmentDto: FiredepartmentDtos.FiredepartmentCreate): FiredepartmentDtos.FiredepartmentResponse

    fun getAllFiredepartments(
        page: Int = 0,
        size: Int = 20,
        sort: String = "firedepartmentId,asc"
    ): Page<FiredepartmentDtos.FiredepartmentResponse>

    fun getFiredepartmentById(firedepartmentId: Int): FiredepartmentDtos.FiredepartmentResponse

    fun updateFiredepartment(
        firedepartmentId: Int,
        firedepartmentDto: FiredepartmentDtos.FiredepartmentPatch
    ): FiredepartmentDtos.FiredepartmentResponse
}