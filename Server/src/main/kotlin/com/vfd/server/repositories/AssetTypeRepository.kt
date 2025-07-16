package com.vfd.server.repositories

import com.vfd.server.entities.AssetType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetTypeRepository : JpaRepository<AssetType, String>