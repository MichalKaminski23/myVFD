package com.vfd.server.repositories

import com.vfd.server.entities.Operation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OperationRepository : JpaRepository<Operation, Int>