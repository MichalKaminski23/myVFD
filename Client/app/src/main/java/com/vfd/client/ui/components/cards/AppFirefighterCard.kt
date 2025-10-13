package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefighterCard(
    firefighter: FirefighterDtos.FirefighterResponse,
    quarterHours: Double? = null,
    actions: @Composable (() -> Unit)? = null,
) {
    val cardTexts = mutableListOf(
        "\uD83D\uDE92 ${firefighter.firedepartmentName}",
        "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${firefighter.role}",
        "⌚ Hours on actions: ${firefighter.hours}",
        "✨ To God for glory, to people for salvation."
    )
    quarterHours?.let {
        cardTexts.add("🗓 Hours for chosen quarter: $it")
    }
    AppCard(cardTexts, actions)
}