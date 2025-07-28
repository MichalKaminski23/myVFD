package com.vfd.server.services

import com.vfd.server.dtos.AssetDto
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AssetMapper
import com.vfd.server.repositories.AssetRepository
import com.vfd.server.repositories.AssetTypeRepository
import com.vfd.server.repositories.FiredepartmentRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class AssetService(
    private val assetRepository: AssetRepository, private val firedepartmentRepository: FiredepartmentRepository,
    private val assetTypeRepository: AssetTypeRepository, val assetMapper: AssetMapper
) {

    fun getAllAssets() = assetRepository.findAll().map(assetMapper::toDto)

    fun getAssetById(id: Int) = assetRepository.findById(id)
        .map(assetMapper::toDto)
        .orElseThrow { ResourceNotFoundException("Asset with id $id not found.") }

    fun createAsset(assetDto: AssetDto) = assetMapper.toDto(
        assetRepository.save(assetMapper.toEntity(assetDto))
    )

    fun updateAsset(id: Int, dto: AssetDto) {
        val asset = assetRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Asset with ID $id not found.") }

        assetMapper.updateEntityFromDto(dto, asset)

        dto.firedepartmentId.let {
            asset.firedepartment = firedepartmentRepository.getReferenceById(it)
        }

        dto.assetType.let {
            asset.assetType = assetTypeRepository.getReferenceById(it)
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