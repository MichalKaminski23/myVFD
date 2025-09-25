package com.vfd.server.services.implementations

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.mappers.InvestmentProposalMapper
import com.vfd.server.repositories.FiredepartmentRepository
import com.vfd.server.repositories.InvestmentProposalRepository
import com.vfd.server.services.InvestmentProposalService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.findByIdOrThrow
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InvestmentProposalServiceImplementation(
    private val investmentProposalRepository: InvestmentProposalRepository,
    private val investmentProposalMapper: InvestmentProposalMapper,
    private val firedepartmentRepository: FiredepartmentRepository
) : InvestmentProposalService {

    override fun createInvestmentProposal(
        emailAddress: String,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse {
        TODO("Not yet implemented")
    }

    override fun getInvestmentProposals(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> {
        TODO("Not yet implemented")
    }

    override fun updateInvestmentProposal(
        emailAddress: String,
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse {
        TODO("Not yet implemented")
    }

    private val INVESTMENT_PROPOSAL_ALLOWED_SORTS = setOf(
        "investmentProposalId",
        "createdAt",
        "firedepartment.firedepartmentId"
    )

    @Transactional
    override fun createInvestmentProposalDev(
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreateDev
    ): InvestmentProposalDtos.InvestmentProposalResponse {

        val investmentProposal = investmentProposalMapper.toInvestmentProposalEntityDev(investmentProposalDto)

        val firedepartment = firedepartmentRepository.findByIdOrThrow(investmentProposalDto.firedepartmentId)

        investmentProposal.firedepartment = firedepartment

        return investmentProposalMapper.toInvestmentProposalDto(
            investmentProposalRepository.save(investmentProposal)
        )
    }

    @Transactional(readOnly = true)
    override fun getAllInvestmentProposalsDev(
        page: Int,
        size: Int,
        sort: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INVESTMENT_PROPOSAL_ALLOWED_SORTS,
            defaultSort = "investmentProposalId,asc",
            maxSize = 200
        )

        return investmentProposalRepository.findAll(pageable)
            .map(investmentProposalMapper::toInvestmentProposalDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getInvestmentProposalByIdDev(
        investmentProposalId: Int
    ): InvestmentProposalDtos.InvestmentProposalResponse {

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(investmentProposalId)

        return investmentProposalMapper.toInvestmentProposalDto(investmentProposal)
    }

    @Transactional
    override fun updateInvestmentProposalDev(
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse {

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(investmentProposalId)

        investmentProposalMapper.patchInvestmentProposal(investmentProposalDto, investmentProposal)

        return investmentProposalMapper.toInvestmentProposalDto(
            investmentProposalRepository.save(investmentProposal)
        )
    }
}