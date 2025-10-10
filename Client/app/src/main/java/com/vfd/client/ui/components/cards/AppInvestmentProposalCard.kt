package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos

@Composable
fun AppInvestmentProposalCard(
    investmentProposal: InvestmentProposalDtos.InvestmentProposalResponse,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "📝${investmentProposal.description}",
            "💰 Amount: ${investmentProposal.amount}",
            "\uD83D\uDCC6 Date: ${investmentProposal.submissionDate}",
            "✅ Status: ${investmentProposal.status}",
            "\uD83D\uDD22 Total votes: ${investmentProposal.votesCount}",
            "\uD83D\uDCB0 Votes for: ${investmentProposal.votesYesCount}",
            "\uD83E\uDDD1\uD83C\uDFFF My vote: ${if (investmentProposal.myVote == true) "Yes" else "No"} ",
        ),
        actions = actions
    )
}