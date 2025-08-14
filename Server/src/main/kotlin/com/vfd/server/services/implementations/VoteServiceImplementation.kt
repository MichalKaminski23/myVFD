package com.vfd.server.services.implementations

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.entities.Vote
import com.vfd.server.exceptions.ResourceNotFoundException
import com.vfd.server.mappers.VoteMapper
import com.vfd.server.repositories.FirefighterRepository
import com.vfd.server.repositories.InvestmentProposalRepository
import com.vfd.server.repositories.VoteRepository
import com.vfd.server.services.VoteService
import com.vfd.server.shared.PageResponse
import com.vfd.server.shared.PaginationUtils
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

    private val VOTE_ALLOWED_SORTS = setOf(
        "voteId",
        "voteDate",
        "value",
        "proposal.proposalId",
        "firefighter.firefighterId"
    )

    @Transactional
    override fun createVote(voteDto: VoteDtos.VoteCreate): VoteDtos.VoteResponse {

        val vote: Vote = voteMapper.toVoteEntity(voteDto)

        val proposal = investmentProposalRepository.findById(voteDto.investmentProposalId)
            .orElseThrow { ResourceNotFoundException("InvestmentProposal", "id", voteDto.investmentProposalId) }
        vote.proposal = proposal

        val firefighter = firefighterRepository.findById(voteDto.firefighterId)
            .orElseThrow { ResourceNotFoundException("Firefighter", "id", voteDto.firefighterId) }
        vote.firefighter = firefighter

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }

    @Transactional(readOnly = true)
    override fun getAllVotes(page: Int, size: Int, sort: String): PageResponse<VoteDtos.VoteResponse> {

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
    override fun getVoteById(voteId: Int): VoteDtos.VoteResponse {

        val vote = voteRepository.findById(voteId)
            .orElseThrow { ResourceNotFoundException("Vote", "id", voteId) }

        return voteMapper.toVoteDto(vote)
    }

    @Transactional
    override fun updateVote(voteId: Int, voteDto: VoteDtos.VotePatch): VoteDtos.VoteResponse {

        val vote = voteRepository.findById(voteId)
            .orElseThrow { ResourceNotFoundException("Vote", "id", voteId) }

        voteMapper.patchVote(voteDto, vote)

        return voteMapper.toVoteDto(voteRepository.save(vote))
    }
}