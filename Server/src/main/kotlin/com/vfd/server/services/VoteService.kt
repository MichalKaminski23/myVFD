package com.vfd.server.services

import com.vfd.server.dtos.VoteDtos
import com.vfd.server.shared.PageResponse

interface VoteService {

    fun createVote(
        emailAddress: String,
        voteDto: VoteDtos.VoteCreate
    ): VoteDtos.VoteResponse

    fun getVotes(
        page: Int = 0,
        size: Int = 20,
        sort: String = "voteId,asc",
        emailAddress: String
    ): PageResponse<VoteDtos.VoteResponse>

    fun updateVote(
        emailAddress: String,
        voteId: Int,
        voteDto: VoteDtos.VotePatch
    ): VoteDtos.VoteResponse

    fun createVoteDev(voteDto: VoteDtos.VoteCreateDev): VoteDtos.VoteResponse

    fun getAllVotesDev(
        page: Int = 0,
        size: Int = 20,
        sort: String = "voteId,asc"
    ): PageResponse<VoteDtos.VoteResponse>

    fun getVoteByIdDev(voteId: Int): VoteDtos.VoteResponse

    fun updateVoteDev(voteId: Int, voteDto: VoteDtos.VotePatch): VoteDtos.VoteResponse
}