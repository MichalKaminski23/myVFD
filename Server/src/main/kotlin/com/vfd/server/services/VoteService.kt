package com.vfd.server.services

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.shared.PageResponse

interface VoteService {

    fun createVote(voteDto: VoteDtos.VoteCreate): VoteDtos.VoteResponse

    fun getAllVotes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "voteId,asc"
    ): PageResponse<VoteDtos.VoteResponse>

    fun getVoteById(voteId: Int): VoteDtos.VoteResponse

    fun updateVote(voteId: Int, voteDto: VoteDtos.VotePatch): VoteDtos.VoteResponse
}