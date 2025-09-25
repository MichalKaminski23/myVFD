package com.vfd.server.repositories

import com.vfd.server.entities.Firefighter
import com.vfd.server.entities.FirefighterStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirefighterRepository : JpaRepository<Firefighter, Int> {

    fun findAllByFiredepartmentFiredepartmentIdAndStatus(
        firedepartmentId: Int,
        status: FirefighterStatus,
        pageable: Pageable
    ): Page<Firefighter>
}