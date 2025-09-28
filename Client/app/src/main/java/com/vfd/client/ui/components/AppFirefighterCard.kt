package com.vfd.client.ui.components

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefighterCard(firefighter: FirefighterDtos.FirefighterResponse) {
    AppCard(
        "\uD83D\uDE92 ${firefighter.firedepartmentName}",
        "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${firefighter.role}",
        "✨ To God for glory, to people for salvation.",
        null
    )
}