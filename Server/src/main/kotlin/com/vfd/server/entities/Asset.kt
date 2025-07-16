package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Assets")
class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    var assetId: Int? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "firedepartment_id", nullable = false)
    lateinit var firedepartment: Firedepartment

    @Column(name = "name", length = 128, nullable = false)
    lateinit var name: String

    @ManyToOne
    @JoinColumn(name = "asset_type", nullable = false)
    lateinit var assetType: AssetType

    @Column(name = "description", length = 512, nullable = false)
    lateinit var description: String
}