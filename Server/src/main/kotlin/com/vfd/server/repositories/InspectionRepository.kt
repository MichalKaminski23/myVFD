package com.vfd.server.repositories

import com.vfd.server.entities.Inspection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InspectionRepository : JpaRepository<Inspection, Int> {

    fun findAllByAssetFiredepartmentFiredepartmentId(
        firedepartmentId: Int,
        pageable: Pageable
    ): Page<Inspection>
}