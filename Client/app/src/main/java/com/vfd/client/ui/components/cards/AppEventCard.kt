package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.EventDtos

@Composable
fun AppEventCard(
    event: EventDtos.EventResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDCC5 ${event?.header}",
            "\uD83D\uDCC6 Date: ${event?.eventDate}",
            "✏\uFE0F Description: ${event?.description}"
        ),
        actions = actions
    )
}