package com.vfd.client.data.remote.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.math.BigDecimal

object InvestmentProposalDtos {

    @Serializable
    data class InvestmentProposalCreate(
        val description: String,

        @Serializable(with = BigDecimalSerializer::class)
        val amount: BigDecimal
    )

    @Serializable
    data class InvestmentProposalPatch(
        val description: String? = null,

        @Serializable(with = BigDecimalSerializer::class)
        val amount: BigDecimal? = null,

        val status: String? = null
    )

    @Serializable
    data class InvestmentProposalResponse(
        val investmentProposalId: Int,

        val firedepartmentId: Int,

        val description: String,

        @Serializable(with = BigDecimalSerializer::class)
        val amount: BigDecimal? = null,

        val submissionDate: LocalDateTime,

        val status: String,

        val votesCount: Int,

        val votesYesCount: Int,

        val myVote: Boolean? = null
    )
}