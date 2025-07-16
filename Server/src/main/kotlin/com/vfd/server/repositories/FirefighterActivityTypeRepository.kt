package com.vfd.server.repositories

import com.vfd.server.entities.FirefighterActivityType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FirefighterActivityTypeRepository : JpaRepository<FirefighterActivityType, String>