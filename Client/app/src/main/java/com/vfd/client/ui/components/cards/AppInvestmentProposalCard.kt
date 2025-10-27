package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.InvestmentProposalDtos

@Composable
fun AppInvestmentProposalCard(
    investmentProposal: InvestmentProposalDtos.InvestmentProposalResponse,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "📝${investmentProposal.description}",
            "💰 ${stringResource(id = R.string.amount)}: ${investmentProposal.amount}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_date)}: ${investmentProposal.submissionDate}",
            "✅ ${
                when (investmentProposal.status) {
                    "PENDING" -> stringResource(id = R.string.item_status) + ": " + stringResource(
                        id = R.string.pending
                    )

                    "APPROVED" -> stringResource(id = R.string.item_status) + ": " + stringResource(
                        id = R.string.approved
                    )

                    "REJECTED" -> stringResource(id = R.string.item_status) + ": " + stringResource(
                        id = R.string.rejected
                    )

                    "CANCELLED" -> stringResource(id = R.string.item_status) + ": " + stringResource(
                        id = R.string.cancelled
                    )

                    else -> investmentProposal.status
                }
            }",
            "\uD83D\uDD22 ${stringResource(id = R.string.counter)}: ${investmentProposal.votesCount}",
            "\uD83D\uDCB0 ${stringResource(id = R.string.votes_yes)}: ${investmentProposal.votesYesCount}",
            "\uD83E\uDDD1\uD83C\uDFFF ${stringResource(id = R.string.vote_my)}: ${
                if (investmentProposal.myVote == true) stringResource(
                    id = R.string.yes
                ) else stringResource(id = R.string.no)
            } ",
        ),
        actions = actions
    )
}