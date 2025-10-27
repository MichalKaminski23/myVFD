package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.EventDtos

@Composable
fun AppEventCard(
    event: EventDtos.EventResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDCC5 ${event?.header}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_date)}: ${event?.eventDate}",
            "✏\uFE0F ${stringResource(id = R.string.item_description)}: ${event?.description}"
        ),
        actions = actions
    )
}