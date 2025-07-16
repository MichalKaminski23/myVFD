package com.vfd.server.repositories

import com.vfd.server.entities.InspectionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InspectionTypeRepository : JpaRepository<InspectionType, String>