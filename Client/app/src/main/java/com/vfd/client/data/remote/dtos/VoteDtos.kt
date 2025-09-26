package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

object VoteDtos {

    @Serializable
    data class VoteCreate(
        val investmentProposalId: Int,

        val voteValue: Boolean
    )

    @Serializable
    data class VotePatch(
        val voteValue: Boolean? = null
    )

    @Serializable
    data class VoteResponse(
        val voteId: Int,

        val investmentProposalId: Int,

        val firefighterId: Int,

        val voteValue: Boolean,

        val voteDate: LocalDateTime
    )
}