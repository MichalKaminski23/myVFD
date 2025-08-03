package com.vfd.server.services

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AssetMapper
import com.vfd.server.repositories.AssetRepository
import com.vfd.server.repositories.AssetTypeRepository
import com.vfd.server.repositories.FiredepartmentRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetService(
    private val assetRepository: AssetRepository,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val assetTypeRepository: AssetTypeRepository,
    val assetMapper: AssetMapper
) {

    fun getAllAssets(): List<AssetDtos.AssetResponse> =
        assetRepository.findAll().map(assetMapper::toAssetDto)

    fun getAssetById(id: Int): AssetDtos.AssetResponse =
        assetRepository.findById(id)
            .map(assetMapper::toAssetDto)
            .orElseThrow { ResourceNotFoundException("Asset with id $id not found.") }

    @Transactional
    fun createAsset(dto: AssetDtos.AssetCreate): AssetDtos.AssetResponse {
        val asset = assetMapper.toAssetEntity(dto)

        val firedepartment = firedepartmentRepository.findById(dto.firedepartmentId!!)
            .orElseThrow { EntityNotFoundException("Firedepartment with ID ${dto.firedepartmentId} not found.") }

        val assetType = assetTypeRepository.findByAssetType(dto.assetType)
            ?: throw EntityNotFoundException("AssetType '${dto.assetType}' not found.")

        asset.firedepartment = firedepartment
        asset.assetType = assetType

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    @Transactional
    fun updateAsset(id: Int, dto: AssetDtos.AssetPatch) {
        val asset = assetRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Asset with ID $id not found.") }

        assetMapper.patchAsset(dto, asset)

        dto.firedepartmentId?.let {
            val firedepartment = firedepartmentRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Firedepartment with ID $it not found.") }
            asset.firedepartment = firedepartment
        }

        dto.assetType?.let {
            val assetType = assetTypeRepository.findByAssetType(it)
                ?: throw EntityNotFoundException("AssetType '$it' not found.")
            asset.assetType = assetType
        }

        assetRepository.save(asset)
    }

    fun deleteAsset(id: Int) {
        if (!assetRepository.existsById(id)) {
            throw ResourceNotFoundException("Asset with id $id not found.")
        }
        assetRepository.deleteById(id)
    }
}