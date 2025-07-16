package com.vfd.server.repositories

import com.vfd.server.entities.FirefighterActivity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirefighterActivityRepository : JpaRepository<FirefighterActivity, Integer>