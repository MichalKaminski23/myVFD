package com.vfd.server.repositories

import com.vfd.server.entities.Firedepartment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FiredepartmentRepository : JpaRepository<Firedepartment, Int>