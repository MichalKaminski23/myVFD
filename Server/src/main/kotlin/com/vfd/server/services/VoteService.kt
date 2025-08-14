package com.vfd.server.services

import com.vfd.server.dtos.VoteDtos
import org.springframework.data.domain.Page

interface VoteService {

    fun createVote(voteDto: VoteDtos.VoteCreate): VoteDtos.VoteResponse

    fun getAllVotes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "voteId,asc"
    ): Page<VoteDtos.VoteResponse>

    fun getVoteById(voteId: Int): VoteDtos.VoteResponse

    fun updateVote(voteId: Int, voteDto: VoteDtos.VotePatch): VoteDtos.VoteResponse
}