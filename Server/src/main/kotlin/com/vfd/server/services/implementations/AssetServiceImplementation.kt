package com.vfd.server.services.implementations

import com.vfd.server.dtos.AssetDtos
import com.vfd.server.exceptions.ForbiddenException
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.AssetMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.AssetService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.toPageResponse
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

        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw ResourceNotFoundException("User", "email", emailAddress)

        val firefighter = firefighterRepository.findById(user.userId!!)
            .orElseThrow { ResourceNotFoundException("Firefighter", "userId", user.userId!!) }

        val firedepartment = firefighter.firedepartment
            ?: throw ResourceNotFoundException("Firedepartment", "id", firefighter.firedepartment!!.firedepartmentId!!)

        val assetType = assetTypeRepository.findById(assetDto.assetType)
            .orElseThrow { ResourceNotFoundException("AssetType", "code", assetDto.assetType) }

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

        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw ResourceNotFoundException("User", "email", emailAddress)

        val firefighter = firefighterRepository.findById(user.userId!!)
            .orElseThrow { ResourceNotFoundException("Firefighter", "userId", user.userId!!) }

        val firedepartmentId = firefighter.firedepartment?.firedepartmentId
            ?: throw ResourceNotFoundException(
                "Firefighter",
                "firedepartment",
                firefighter.firedepartment!!.firedepartmentId!!
            )

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

        val user = userRepository.findByEmailAddressIgnoreCase(emailAddress)
            ?: throw ResourceNotFoundException("User", "email", emailAddress)

        val firefighter = firefighterRepository.findById(user.userId!!)
            .orElseThrow { ResourceNotFoundException("Firefighter", "userId", user.userId!!) }

        val asset = assetRepository.findById(assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", assetId) }

        assetMapper.patchAsset(assetDto, asset)

        val firedepartmentId = firefighter.firedepartment?.firedepartmentId
            ?: throw ResourceNotFoundException("Firedepartment", "userId", user.userId!!)

        if (asset.firedepartment?.firedepartmentId != firedepartmentId) {
            throw ForbiddenException("You cannot modify assets of another firedepartment.")
        }

        assetDto.assetType
            ?.takeIf { it != asset.assetType?.assetType }
            ?.let { code ->
                val assetType = assetTypeRepository.findById(code)
                    .orElseThrow { ResourceNotFoundException("Asset's type", "code", code) }
                asset.assetType = assetType
            }

        return assetMapper.toAssetDto(assetRepository.save(asset))
    }

    private val ASSET_ALLOWED_SORTS = setOf("assetId", "name", "assetType.assetType")

    @Transactional
    override fun createAssetDev(assetDto: AssetDtos.AssetCreateDev): AssetDtos.AssetResponse {

        val asset = assetMapper.toAssetEntityDev(assetDto)

        val firedepartment = firedepartmentRepository.findById(assetDto.firedepartmentId)
            .orElseThrow { ResourceNotFoundException("Firedepartment", "id", assetDto.firedepartmentId) }
        asset.firedepartment = firedepartment

        val assetType = assetTypeRepository.findById(assetDto.assetType)
            .orElseThrow { ResourceNotFoundException("Asset's type", "code", assetDto.assetType) }
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

        val asset = assetRepository.findById(assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", assetId) }

        return assetMapper.toAssetDto(asset)
    }

    @Transactional
    override fun updateAssetDev(assetId: Int, assetDto: AssetDtos.AssetPatch): AssetDtos.AssetResponse {

        val asset = assetRepository.findById(assetId)
            .orElseThrow { ResourceNotFoundException("Asset", "id", assetId) }

        assetMapper.patchAsset(assetDto, asset)

        assetDto.assetType
            ?.takeIf { it != asset.assetType?.assetType }
            ?.let { code ->
                val assetType = assetTypeRepository.findById(code)
                    .orElseThrow { ResourceNotFoundException("Asset's type", "code", code) }
                asset.assetType = assetType
            }


        return assetMapper.toAssetDto(assetRepository.save(asset))
    }
}