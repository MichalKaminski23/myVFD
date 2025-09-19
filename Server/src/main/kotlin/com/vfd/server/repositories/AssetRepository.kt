package com.vfd.server.repositories

import com.vfd.server.entities.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<Asset, Int> {

    fun findAllByFiredepartmentFiredepartmentId(firedepartmentId: Int): List<Asset>
}