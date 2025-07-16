package com.vfd.server.entities

import jakarta.persistence.*

@Entity
@Table(name = "Assets")
class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    var assetId: Int? = null

    @ManyToOne
    @JoinColumn(name = "firedepartment_id")
    var firedepartment: Firedepartment? = null

    @Column(name = "name", length = 128)
    var name: String? = null

    @ManyToOne
    @JoinColumn(name = "asset_type")
    var assetType: AssetType? = null

    @Column(name = "description", length = 512)
    var description: String? = null
}