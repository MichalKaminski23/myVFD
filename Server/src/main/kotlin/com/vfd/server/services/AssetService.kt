package com.vfd.server.services

import com.vfd.server.dtos.AssetDto
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
    private val assetRepository: AssetRepository, private val firedepartmentRepository: FiredepartmentRepository,
    private val assetTypeRepository: AssetTypeRepository, val assetMapper: AssetMapper
) {

    fun getAllAssets() = assetRepository.findAll().map(assetMapper::fromAssetToAssetDto)

    fun getAssetById(id: Int) = assetRepository.findById(id)
        .map(assetMapper::fromAssetToAssetDto)
        .orElseThrow { ResourceNotFoundException("Asset with id $id not found.") }

    @Transactional
    fun createAsset(assetDto: AssetDto) = assetMapper.fromAssetToAssetDto(
        assetRepository.save(assetMapper.fromAssetDtoToAsset(assetDto))
    )

    @Transactional
    fun updateAsset(id: Int, dto: AssetDto) {
        val asset = assetRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Asset with ID $id not found.") }

        assetMapper.updateAssetFromAssetDto(dto, asset)

        dto.firedepartmentId?.let {
            val firedepartment = firedepartmentRepository.findById(it)
                .orElseThrow { EntityNotFoundException("Firedepartment with ID $it not found.") }
            asset.firedepartment = firedepartment
        }

        dto.assetType?.let {
            val assetType = assetTypeRepository.findByAssetType(it)
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