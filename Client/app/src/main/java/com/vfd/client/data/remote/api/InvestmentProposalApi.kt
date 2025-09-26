package com.vfd.client.data.remote.api

import com.vfd.client.data.remote.dtos.InvestmentProposalDtos
import com.vfd.client.utils.PageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InvestmentProposalApi {

    @POST("api/investment-proposals/my")
    suspend fun createInvestmentProposal(
        @Body investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse

    @GET("api/investment-proposals/my")
    suspend fun getInvestmentProposals(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "submissionDate,desc"
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>

    @PATCH("api/investment-proposals/my/{proposalId}")
    suspend fun updateInvestmentProposal(
        @Path("proposalId") proposalId: Int,
        @Body investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse
}