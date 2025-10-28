package com.vfd.server.dtos

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

object VoteDtos {

    @Schema(description = "DTO used for casting a new vote on an investment proposal")
    data class VoteCreate(
        @field:NotNull(message = "{vote.investmentProposalId.not_null}")
        @field:Schema(description = "ID of the investment proposal being voted on", example = "7")
        var investmentProposalId: Int,

        @field:NotNull(message = "{vote.voteValue.not_null}")
        @field:Schema(description = "Value of the vote (true for yes, false for no)", example = "true")
        var voteValue: Boolean
    )

    @Schema(description = "DTO used for modifying an existing vote")
    data class VotePatch(
        @field:Schema(description = "Value of the vote (true for yes, false for no)", example = "true")
        val voteValue: Boolean? = null
    )

    @Schema(description = "DTO used for returning vote information")
    data class VoteResponse(
        @field:Schema(description = "Unique identifier of the vote", example = "7")
        val voteId: Int,

        @field:Schema(description = "Proposal on which the vote was cast", example = "3")
        val investmentProposalId: Int,

        @field:Schema(description = "Firefighter who cast the vote", example = "5")
        val firefighterId: Int,

        @field:Schema(description = "Value of the vote (true for yes, false for no)", example = "true")
        val voteValue: Boolean,

        @field:Schema(description = "Timestamp when the vote was cast", example = "2025-08-03T15:00:00")
        val voteDate: LocalDateTime
    )

    @Schema(description = "DTO used for casting a new vote on an investment proposal for development purposes")
    data class VoteCreateDev(
        @field:NotNull(message = "{vote.investmentProposalId.not_null}")
        @field:Schema(description = "ID of the investment proposal being voted on", example = "7")
        var investmentProposalId: Int,

        @field:NotNull(message = "{vote.firefighterId.not_null}")
        @field:Schema(description = "ID of the firefighter casting the vote", example = "7")
        var firefighterId: Int,

        @field:NotNull(message = "{vote.voteValue.not_null}")
        @field:Schema(description = "Value of the vote (true for yes, false for no)", example = "true")
        var voteValue: Boolean
    )
}