package com.vfd.client.data.repositories

import com.vfd.client.data.remote.api.InvestmentProposalApi
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InvestmentProposalRepository @Inject constructor(
    private val investmentProposalApi: InvestmentProposalApi,
    json: Json
) : BaseRepository(json) {

    suspend fun createInvestmentProposal(
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): ApiResult<InvestmentProposalDtos.InvestmentProposalResponse> =
        safeApiCall { investmentProposalApi.createInvestmentProposal(investmentProposalDto) }

    suspend fun getAllInvestmentProposals(
        page: Int = 0,
        size: Int = 20,
        sort: String = "investmentProposalId,asc"
    ): ApiResult<PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>> =
        safeApiCall { investmentProposalApi.getAllInvestmentProposals(page, size, sort) }

    suspend fun getInvestmentProposalById(
        investmentProposalId: Int
    ): ApiResult<InvestmentProposalDtos.InvestmentProposalResponse> =
        safeApiCall { investmentProposalApi.getInvestmentProposalById(investmentProposalId) }

    suspend fun updateInvestmentProposal(
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): ApiResult<InvestmentProposalDtos.InvestmentProposalResponse> =
        safeApiCall {
            investmentProposalApi.updateInvestmentProposal(
                investmentProposalId,
                investmentProposalDto
            )
        }
}