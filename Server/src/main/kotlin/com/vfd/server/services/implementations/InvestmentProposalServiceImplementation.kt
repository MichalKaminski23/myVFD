package com.vfd.server.services.implementations

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.entities.InvestmentProposalStatus
import com.vfd.server.exceptions.InvalidStatusException
import com.vfd.server.mappers.InvestmentProposalMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.InvestmentProposalService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InvestmentProposalServiceImplementation(
    private val investmentProposalRepository: InvestmentProposalRepository,
    private val investmentProposalMapper: InvestmentProposalMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository,
    private val voteRepository: VoteRepository
) : InvestmentProposalService {

    fun validateStatus(status: String?) {
        try {
            InvestmentProposalStatus.valueOf(status!!)
        } catch (exception: IllegalArgumentException) {
            throw InvalidStatusException(
                "Invalid status: ${status!!}. Allowed: ${
                    InvestmentProposalStatus.entries.joinToString()
                }"
            )
        }
    }

    @Transactional
    override fun createInvestmentProposal(
        emailAddress: String,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalCreate
    ): InvestmentProposalDtos.InvestmentProposalResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartment = firefighter.requireFiredepartment()

        val investmentProposal = investmentProposalMapper.toInvestmentProposalEntity(investmentProposalDto).apply {
            this.firedepartment = firedepartment
            this.status = InvestmentProposalStatus.PENDING
        }

        return investmentProposalMapper.toInvestmentProposalDto(
            investmentProposalRepository.save(investmentProposal)
        )
    }

    @Transactional(readOnly = true)
    override fun getInvestmentProposals(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<InvestmentProposalDtos.InvestmentProposalResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = INVESTMENT_PROPOSAL_ALLOWED_SORTS,
            defaultSort = "submissionDate,desc",
            maxSize = 200
        )

        return investmentProposalRepository.findAllByFiredepartmentFiredepartmentId(firedepartmentId, pageable)
            .map { investmentProposal ->
                val investmentProposalDto = investmentProposalMapper.toInvestmentProposalDto(investmentProposal)

                val votesCount =
                    voteRepository.countByInvestmentProposalInvestmentProposalId(investmentProposal.investmentProposalId!!)

                val votesYesCount = voteRepository.countByInvestmentProposalInvestmentProposalIdAndVoteValue(
                    investmentProposal.investmentProposalId!!,
                    true
                )

                val myVote = voteRepository.findByInvestmentProposalInvestmentProposalIdAndFirefighterFirefighterId(
                    investmentProposal.investmentProposalId!!,
                    firefighter.firefighterId!!
                )?.voteValue

                investmentProposalDto.copy(votesCount = votesCount, votesYesCount = votesYesCount, myVote = myVote)
            }
            .toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun updateInvestmentProposal(
        emailAddress: String,
        investmentProposalId: Int,
        investmentProposalDto: InvestmentProposalDtos.InvestmentProposalPatch
    ): InvestmentProposalDtos.InvestmentProposalResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(investmentProposalId)

        investmentProposal.requireSameFiredepartment(firedepartmentId)

        validateStatus(investmentProposalDto.status)

        investmentProposalMapper.patchInvestmentProposal(investmentProposalDto, investmentProposal)

        return investmentProposalMapper.toInvestmentProposalDto(
            investmentProposalRepository.save(investmentProposal)
        )
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
        investmentProposal.status = InvestmentProposalStatus.PENDING

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