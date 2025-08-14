package com.vfd.server.services

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.shared.PageResponse

interface InvestmentProposalService {

    fun createInvestmentProposal(investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate): InvestmentProposalDtos.InvestmentProposalResponse

    fun getAllInvestmentProposals(
        page: Int = 0,
        size: Int = 20,
        sort: String = "investmentProposalId,asc"
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>

    fun getInvestmentProposalById(investmentProposalId: Int): InvestmentProposalDtos.InvestmentProposalResponse

    fun updateInvestmentProposal(
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse
}