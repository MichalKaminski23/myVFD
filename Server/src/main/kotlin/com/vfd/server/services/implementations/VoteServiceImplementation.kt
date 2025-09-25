package com.vfd.server.services.implementations

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.mappers.VoteMapper
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.InvestmentProposalRepository
import com.vfd.server.repositories.VoteRepository
import com.vfd.server.services.VoteService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
import com.vfd.server.shared.findByIdOrThrow
import com.vfd.server.shared.toPageResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VoteServiceImplementation(
    private val voteRepository: VoteRepository,
    private val voteMapper: VoteMapper,
    private val investmentProposalRepository: InvestmentProposalRepository,
    private val firefighterRepository: FirefighterRepository
) : VoteService {

    override fun createVote(
        emailAddress: String,
        voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse {
        TODO("Not yet implemented")
    }

    override fun getVotes(
        page: Int,
        size: Int,
        sort: String,
        emailAddress: String
    ): PageResponse<VoteDtos.VoteResponse> {
        TODO("Not yet implemented")
    }

    override fun updateVote(
        emailAddress: String,
        voteId: Int,
        voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse {
        TODO("Not yet implemented")
    }

    private val VOTE_ALLOWED_SORTS = setOf(
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
            allowedFields = VOTE_ALLOWED_SORTS,
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