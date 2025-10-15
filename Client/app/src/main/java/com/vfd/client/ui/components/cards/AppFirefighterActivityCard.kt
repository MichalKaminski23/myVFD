package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos

@Composable
fun AppFirefighterActivityCard(
    activity: FirefighterActivityDtos.FirefighterActivityResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDD75 ${activity?.firefighterActivityTypeName}",
            "\uD83D\uDCC6 Inspection date: ${activity?.activityDate}",
            "\uD83D\uDCC6 Expiration date: ${if (activity?.expirationDate == null) "Not set" else activity.expirationDate}",
            "✏\uFE0F Description: ${activity?.description}",
            "\uD83E\uDDD1\u200D\uD83D\uDE92 Status: ${activity?.status}",
        ),
        actions = actions
    )
}