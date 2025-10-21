package com.vfd.server.repositories

import com.vfd.server.entities.Firefighter
import com.vfd.server.entities.FirefighterRole
import com.vfd.server.entities.FirefighterStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FirefighterRepository : JpaRepository<Firefighter, Int> {

    @Query(
        value = """
            SELECT COALESCE(SUM(
                CASE WHEN o.operation_end IS NOT NULL AND o.operation_end > o.operation_date
                     THEN EXTRACT(EPOCH FROM (o.operation_end - o.operation_date)) / 3600
                     ELSE 0 END
            ), 0)
            FROM Operations o
            JOIN Participations p ON p.operation_id = o.operation_id
            WHERE p.firefighter_id = :firefighterId
              AND EXTRACT(YEAR FROM o.operation_date) = :year
              AND EXTRACT(QUARTER FROM o.operation_date) = :quarter
        """,
        nativeQuery = true
    )
    fun getHoursForQuarter(
        @Param("firefighterId") firefighterId: Int,
        @Param("year") year: Int,
        @Param("quarter") quarter: Int
    ): Double

    fun findAllByFiredepartmentFiredepartmentIdAndStatus(
        firedepartmentId: Int,
        status: FirefighterStatus,
        pageable: Pageable
    ): Page<Firefighter>

    fun existsByFiredepartmentFiredepartmentIdAndRole(
        firedepartmentId: Int,
        role: FirefighterRole
    ): Boolean

    fun existsByFiredepartmentFiredepartmentIdAndRoleAndFirefighterIdNot(
        firedepartmentId: Int,
        role: FirefighterRole,
        firefighterId: Int
    ): Boolean
}