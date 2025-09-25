package com.vfd.server.repositories

import com.vfd.server.entities.FirefighterActivity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirefighterActivityRepository : JpaRepository<FirefighterActivity, Int> {

    fun findAllByFirefighterFirefighterId(firefighterId: Int, pageable: Pageable): Page<FirefighterActivity>
}