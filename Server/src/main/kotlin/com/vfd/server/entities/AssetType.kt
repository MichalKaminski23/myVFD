package com.vfd.server.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "AssetTypes")
class AssetType {
    @Id
    @Column(name = "asset_type", length = 16, unique = true)
    var assetType: String? = null

    @Column(name = "name", length = 64, nullable = false)
    lateinit var name: String
}