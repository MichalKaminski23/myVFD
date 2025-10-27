package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos

@Composable
fun AppFirefighterActivityCard(
    activity: FirefighterActivityDtos.FirefighterActivityResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDD75 ${activity?.firefighterActivityTypeName}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_date)}: ${activity?.activityDate}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_end_date)}: ${if (activity?.expirationDate == null) "Not set" else activity.expirationDate}",
            "✏\uFE0F ${stringResource(id = R.string.item_description)}: ${activity?.description}",
            "\uD83E\uDDD1\u200D\uD83D\uDE92  ${
                when (activity?.status) {
                    "REJECTED" -> stringResource(
                        id = R.string.item_status
                    ) + ": " + stringResource(id = R.string.rejected)

                    "APPROVED" -> stringResource(
                        id = R.string.item_status
                    ) + ": " + stringResource(id = R.string.approved)

                    else -> stringResource(id = R.string.item_status) + ": " + stringResource(
                        id = R.string.pending
                    )
                }
            }"
        ),
        actions = actions
    )
}