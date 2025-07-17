package com.vfd.server.repositories

import com.vfd.server.entities.Inspection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InspectionRepository : JpaRepository<Inspection, Int>