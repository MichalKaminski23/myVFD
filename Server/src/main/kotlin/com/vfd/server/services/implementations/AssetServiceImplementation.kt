package com.vfd.server.services.implementations

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.entities.Asset
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AssetMapper
import com.vfd.server.repositories.AssetRepository
import com.vfd.server.repositories.AssetTypeRepository
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.services.AssetService
import com.vfd.server.shared.PaginationUtils
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetServiceImplementation(
    private val assetRepository: AssetRepository,
    private val assetMapper: AssetMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val assetTypeRepository: AssetTypeRepository
) : AssetService {

    private val ASSET_ALLOWED_SORTS = setOf("assetId", "name", "assetType")

    @Transactional
    override fun createAsset(assetDto: AssetDtos.AssetCreate): AssetDtos.AssetResponse {
        val asset: Asset = assetMapper.toAssetEntity(assetDto)

        val firedepartment = firedepartmentRepository.findById(assetDto.firedepartmentId!!)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", assetDto.firedepartmentId) }
        asset.firedepartment = firedepartment

        val assetType = assetTypeRepository.findById(assetDto.assetType)
            .orElseThrow { ResourceNotFoundException("Asset's type", "type", assetDto.assetType) }
        asset.assetType = assetType

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    @Transactional(readOnly = true)
    override fun getAllAssets(page: Int, size: Int, sort: String): Page<AssetDtos.AssetResponse> {
        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            ASSET_ALLOWED_SORTS,
            "assetId,asc",
            200
        )
        return assetRepository.findAll(pageable).map(assetMapper::toAssetDto)
    }

    @Transactional(readOnly = true)
    override fun getAssetById(assetId: Int): AssetDtos.AssetResponse {
        val asset = assetRepository.findById(assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", assetId) }
        return assetMapper.toAssetDto(asset)
    }

    @Transactional
    override fun updateAsset(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse {
        val asset = assetRepository.findById(assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", assetId) }

        assetMapper.patchAsset(assetDto, asset)

        if (assetDto.firedepartmentId != null) {
            val firedepartment = firedepartmentRepository.findById(assetDto.firedepartmentId)
                .orElseThrow { ResourceNotFoundException("Firedepartment", "id", assetDto.firedepartmentId) }
            asset.firedepartment = firedepartment
        }

        if (assetDto.assetType != null) {
            val assetType = assetTypeRepository.findById(assetDto.assetType)
                .orElseThrow { ResourceNotFoundException("Asset's type", "type", assetDto.assetType) }
            asset.assetType = assetType
        }

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }
}