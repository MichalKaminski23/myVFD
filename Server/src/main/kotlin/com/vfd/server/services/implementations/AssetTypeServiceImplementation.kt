package com.vfd.server.services.implementations

import com.vfd.server.dtos.AssetTypeDtos
import com.vfd.server.mappers.AssetTypeMapper
import com.vfd.server.repositories.AssetTypeRepository
import com.vfd.server.services.AssetTypeService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetTypeServiceImplementation(
    private val assetTypeRepository: AssetTypeRepository,
    private val assetTypeMapper: AssetTypeMapper
) : AssetTypeService {

    private val ASSET_TYPE_ALLOWED_SORTS = setOf("assetType", "name")

    @Transactional
    override fun createAssetType(assetTypeDto: AssetTypeDtos.AssetTypeCreate): AssetTypeDtos.AssetTypeResponse {

        val assetType = assetTypeMapper.toAssetTypeEntity(assetTypeDto)

        assetTypeRepository.assertNotExistsByAssetType(assetTypeDto.assetType.uppercase())

        return assetTypeMapper.toAssetTypeDto(assetTypeRepository.save(assetType))
    }

    @Transactional(readOnly = true)
    override fun getAllAssetTypes(page: Int, size: Int, sort: String): PageResponse<AssetTypeDtos.AssetTypeResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page,
            size,
            sort,
            ASSET_TYPE_ALLOWED_SORTS,
            "assetType,asc",
            200
        )

        return assetTypeRepository.findAll(pageable).map(assetTypeMapper::toAssetTypeDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getAssetTypeByCode(assetTypeCode: String): AssetTypeDtos.AssetTypeResponse {

        val assetType = assetTypeRepository.findByIdOrThrow(assetTypeCode)

        return assetTypeMapper.toAssetTypeDto(assetType)
    }

    @Transactional
    override fun updateAssetType(
        assetTypeCode: String,
        assetTypeDto: AssetTypeDtos.AssetTypePatch
    ): AssetTypeDtos.AssetTypeResponse {

        val assetType = assetTypeRepository.findByIdOrThrow(assetTypeCode)

        assetTypeMapper.patchAssetType(assetTypeDto, assetType)

        return assetTypeMapper.toAssetTypeDto(assetTypeRepository.save(assetType))
    }
}