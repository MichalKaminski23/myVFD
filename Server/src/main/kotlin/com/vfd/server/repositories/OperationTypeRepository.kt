package com.vfd.server.repositories

import com.vfd.server.entities.OperationType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OperationTypeRepository : JpaRepository<OperationType, String>