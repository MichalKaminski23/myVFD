package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefightersCard(
    firefighter: FirefighterDtos.FirefighterResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "👤 ${firefighter.firstName} ${firefighter.lastName}",
            "🚒 Firedepartment: ${firefighter.firedepartmentName}",
            "📧 Email address: ${firefighter.emailAddress}",
            "⌚ Hours on actions: ${firefighter.hours}",
            "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${firefighter.role}"
        ),
        actions
    )
}