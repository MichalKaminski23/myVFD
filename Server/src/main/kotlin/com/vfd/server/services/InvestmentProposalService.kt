package com.vfd.server.services

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.shared.PageResponse

interface InvestmentProposalService {

    fun createInvestmentProposal(
        emailAddress: String,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse

    fun getInvestmentProposals(
        page: Int = 0,
        size: Int = 20,
        sort: String = "investmentProposalId,asc",
        emailAddress: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>

    fun updateInvestmentProposal(
        emailAddress: String,
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse

    fun createInvestmentProposalDev(investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreateDev): InvestmentProposalDtos.InvestmentProposalResponse

    fun getAllInvestmentProposalsDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "investmentProposalId,asc"
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse>

    fun getInvestmentProposalByIdDev(investmentProposalId: Int): InvestmentProposalDtos.InvestmentProposalResponse

    fun updateInvestmentProposalDev(
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse
}