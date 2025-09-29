package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefighterCard(
    firefighter: FirefighterDtos.FirefighterResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83D\uDE92 ${firefighter.firedepartmentName}",
            "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${firefighter.role}",
            "✨ To God for glory, to people for salvation."
        ),
        actions
    )
}