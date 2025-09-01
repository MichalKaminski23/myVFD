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

    @POST("api/investment-proposals")
    suspend fun createInvestmentProposal(
        @Body proposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse

    @GET("api/investment-proposals")
    suspend fun getAllInvestmentProposals(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "investmentProposalId,asc"
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>

    @GET("api/investment-proposals/{proposalId}")
    suspend fun getInvestmentProposalById(
        @Path("proposalId") proposalId: Int
    ): InvestmentProposalDtos.InvestmentProposalResponse

    @PATCH("api/investment-proposals/{proposalId}")
    suspend fun updateInvestmentProposal(
        @Path("proposalId") proposalId: Int,
        @Body proposalPatchDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse
}