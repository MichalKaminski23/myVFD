package com.vfd.server.services.implementations

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.mappers.VoteMapper
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.InvestmentProposalRepository
import com.vfd.server.repositories.UserRepository
import com.vfd.server.repositories.VoteRepository
import com.vfd.server.services.VoteService
import com.vfd.server.shared.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class VoteServiceImplementation(
    private val voteRepository: VoteRepository,
    private val voteMapper: VoteMapper,
    private val investmentProposalRepository: InvestmentProposalRepository,
    private val firefighterRepository: FirefighterRepository,
    private val userRepository: UserRepository
) : VoteService {

    @Transactional
    override fun createVote(
        emailAddress: String,
        voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(voteDto.investmentProposalId)

        investmentProposal.requireSameFiredepartment(firedepartmentId)

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        val vote = voteMapper.toVoteEntity(voteDto).apply {
            this.firefighter = firefighter
            this.investmentProposal = investmentProposal
            this.voteDate = now
        }

        voteRepository.assertNotExistsByFirefighter(voteDto.investmentProposalId, firefighter.firefighterId!!)

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }

    @Transactional(readOnly = true)
    override fun getVotes(
        page: Int,
        size: Int,
        sort: String,
        investmentProposalId: Int,
        emailAddress: String
    ): PageResponse<VoteDtos.VoteResponse> {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(investmentProposalId)

        investmentProposal.requireSameFiredepartment(firedepartmentId)

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "voteId,asc",
            maxSize = 200
        )

        return voteRepository.findAllByInvestmentProposalInvestmentProposalId(
            investmentProposalId,
            pageable
        ).map(voteMapper::toVoteDto).toPageResponse()
    }

    @Transactional
    override fun updateVote(
        emailAddress: String,
        voteId: Int,
        voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse {

        val user = userRepository.findByEmailOrThrow(emailAddress)

        val firefighter = firefighterRepository.findByIdOrThrow(user.userId!!)

        val firefighterId = firefighter.firefighterId!!

        val firedepartmentId = firefighter.requireFiredepartmentId()

        val vote = voteRepository.findByIdOrThrow(voteId)

        vote.requireSameFiredepartment(firedepartmentId)

        vote.requireSameFirefighter(firefighterId)

        val now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

        vote.voteDate = now

        voteMapper.patchVote(voteDto, vote)

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }

    private val sorts = setOf(
        "voteId",
        "voteDate",
        "value",
        "investmentProposal.investmentProposalId",
        "firefighter.firefighterId"
    )

    @Transactional
    override fun createVoteDev(voteDto: VoteDtos.VoteCreateDev): VoteDtos.VoteResponse {

        val vote = voteMapper.toVoteEntityDev(voteDto)

        val investmentProposal = investmentProposalRepository.findByIdOrThrow(voteDto.investmentProposalId)
        vote.investmentProposal = investmentProposal

        val firefighter = firefighterRepository.findByIdOrThrow(voteDto.firefighterId)
        vote.firefighter = firefighter

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }

    @Transactional(readOnly = true)
    override fun getAllVotesDev(page: Int, size: Int, sort: String): PageResponse<VoteDtos.VoteResponse> {

        val pageable = PaginationUtils.toPageRequest(
            page = page,
            size = size,
            sort = sort,
            allowedFields = sorts,
            defaultSort = "voteId,asc",
            maxSize = 200
        )

        return voteRepository.findAll(pageable)
            .map(voteMapper::toVoteDto).toPageResponse()
    }

    @Transactional(readOnly = true)
    override fun getVoteByIdDev(voteId: Int): VoteDtos.VoteResponse {

        val vote = voteRepository.findByIdOrThrow(voteId)

        return voteMapper.toVoteDto(vote)
    }

    @Transactional
    override fun updateVoteDev(voteId: Int, voteDto: VoteDtos.VotePatch): VoteDtos.VoteResponse {

        val vote = voteRepository.findByIdOrThrow(voteId)

        voteMapper.patchVote(voteDto, vote)

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }
}