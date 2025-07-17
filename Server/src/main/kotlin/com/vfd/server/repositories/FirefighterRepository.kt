package com.vfd.server.repositories

import com.vfd.server.entities.Firefighter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirefighterRepository : JpaRepository<Firefighter, Int>