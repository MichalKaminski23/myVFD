package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.FirefighterActivityTypeDtos

@Composable
fun AppFirefighterActivityTypeCard(
    firefighterActivityType: FirefighterActivityTypeDtos.FirefighterActivityTypeResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83D\uDDC2\uFE0F ${firefighterActivityType.firefighterActivityType}",
            "\uD83C\uDF89 ${firefighterActivityType.name}"
        ),
        actions
    )
}