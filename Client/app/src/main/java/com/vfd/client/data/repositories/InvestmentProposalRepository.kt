package com.vfd.client.data.repositories

import android.content.Context
import com.vfd.client.data.remote.api.InvestmentProposalApi
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.utils.ApiResult
import com.vfd.client.utils.PageResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class InvestmentProposalRepository @Inject constructor(
    private val investmentProposalApi: InvestmentProposalApi,
    json: Json,
    @ApplicationContext override val context: Context
) : BaseRepository(json, context) {

    suspend fun createInvestmentProposal(
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): ApiResult<InvestmentProposalDtos.InvestmentProposalResponse> =
        safeApiCall { investmentProposalApi.createInvestmentProposal(investmentProposalDto) }

    suspend fun getInvestmentProposals(
        page: Int = 0,
        size: Int = 20,
        sort: String = "submissionDate,desc"
    ): ApiResult<PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>> =
        safeApiCall { investmentProposalApi.getInvestmentProposals(page, size, sort) }

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