package com.vfd.server.services.implementations

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.mappers.AssetMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.AssetService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetServiceImplementation(
    private val assetRepository: AssetRepository,
    private val assetMapper: AssetMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val assetTypeRepository: AssetTypeRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository
) : AssetService {

    @Transactional
    override fun createAsset(
        emailAddress: String,
        assetDto: AssetDtos.AssetCreate
    ): AssetDtos.AssetResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartment = firefighter.requireFiredepartment()

        val assetType = assetTypeRepository.findByIdOrThrow(assetDto.assetType)

        val asset = assetMapper.toAssetEntity(assetDto).apply {
            this.firedepartment = firedepartment
            this.assetType = assetType
        }

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    @Transactional(readOnly = true)
    override fun getAssets(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<AssetDtos.AssetResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            ASSET_ALLOWED_SORTS,
            "name,asc",
            200
        )

        return assetRepository
            .findAllByFiredepartmentFiredepartmentId(firedepartmentId, pageable)
            .map(assetMapper::toAssetDto)
            .toPageResponse()
    }

    @Transactional
    override fun updateAsset(
        emailAddress: String,
        assetId: Int,
        assetDto: AssetDtos.AssetPatch
    ): AssetDtos.AssetResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val asset = assetRepository.findByIdOrThrow(assetId)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        asset.requireSameFiredepartment(firedepartmentId)

        assetMapper.patchAsset(assetDto, asset)

        assetDto.assetType
            ?.takeIf { it != asset.assetType?.assetType }
            ?.let { code ->
                val assetType = assetTypeRepository.findByIdOrThrow(code)
                asset.assetType = assetType
            }

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    private val ASSET_ALLOWED_SORTS = setOf("assetId", "name", "assetType.assetType")

    @Transactional
    override fun createAssetDev(assetDto: AssetDtos.AssetCreateDev): AssetDtos.AssetResponse {

        val asset = assetMapper.toAssetEntityDev(assetDto)

        val firedepartment = firedepartmentRepository.findByIdOrThrow(assetDto.firedepartmentId)
        asset.firedepartment = firedepartment

        val assetType = assetTypeRepository.findByIdOrThrow(assetDto.assetType)
        asset.assetType = assetType

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    @Transactional(readOnly = true)
    override fun getAllAssetsDev(page: Int, size: Int, sort: String): PageResponse<AssetDtos.AssetResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            ASSET_ALLOWED_SORTS,
            "assetId,asc",
            200
        )

        return assetRepository.findAll(pageable).map(assetMapper::toAssetDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getAssetByIdDev(assetId: Int): AssetDtos.AssetResponse {

        val asset = assetRepository.findByIdOrThrow(assetId)

        return assetMapper.toAssetDto(asset)
    }

    @Transactional
    override fun updateAssetDev(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse {

        val asset = assetRepository.findByIdOrThrow(assetId)

        assetMapper.patchAsset(assetDto, asset)

        assetDto.assetType
            ?.takeIf { it != asset.assetType?.assetType }
            ?.let { code ->
                val assetType = assetTypeRepository.findByIdOrThrow(code)
                asset.assetType = assetType
            }

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }
}