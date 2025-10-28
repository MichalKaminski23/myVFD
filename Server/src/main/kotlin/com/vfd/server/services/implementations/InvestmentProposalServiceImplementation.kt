package com.vfd.server.services.implementations

import com.vfd.server.dtos.InvestmentProposalDtos
import com.vfd.server.entities.FirefighterStatus
import com.vfd.server.entities.InvestmentProposalStatus
import com.vfd.server.exceptions.InvalidStatusException
import com.vfd.server.mappers.InvestmentProposalMapper
import com.vfd.server.repositories.*
import com.vfd.server.services.InvestmentProposalService
import com.vfd.server.shared.*
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class InvestmentProposalServiceImplementation(
    private val investmentProposalRepository: InvestmentProposalRepository,
    private val investmentProposalMapper: InvestmentProposalMapper,
    private val firedepartmentRepository: FiredepartmentRepository,
    private val userRepository: UserRepository,
    private val firefighterRepository: FirefighterRepository,
    private val voteRepository: VoteRepository,
    private val messageSource: MessageSource
) : InvestmentProposalService {

    fun validateStatus(status: String?) {
        if (status == null) return
        try {
            InvestmentProposalStatus.valueOf(status)
        } catch (_: IllegalArgumentException) {
            val locale = LocaleContextHolder.getLocale()
            val allowed = FirefighterStatus.entries.joinToString()
            val message = messageSource.getMessage(
                "invalid.status",
                arrayOf(status, allowed),
                "Invalid status: $status. Allowed: $allowed",
                locale
            )
            throw InvalidStatusException(message!!)
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

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        val investmentProposal = investmentProposalMapper.toInvestmentProposalEntity(investmentProposalDto).apply {
            this.firedepartment = firedepartment
            this.submissionDate = now
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
            allowedFields = sorts,
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

    @Transactional
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

    private val sorts = setOf(
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

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        investmentProposal.firedepartment = firedepartment
        investmentProposal.submissionDate = now
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
            allowedFields = sorts,
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