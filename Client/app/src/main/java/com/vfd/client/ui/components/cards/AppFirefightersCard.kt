package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefightersCard(
    firefighter: FirefighterDtos.FirefighterResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "👤 ${firefighter.firstName} ${firefighter.lastName}",
            "🚒 ${firefighter.firedepartmentName}",
            "📧 ${stringResource(id = R.string.email_address)}: ${firefighter.emailAddress}",
            "⌚ ${stringResource(id = R.string.hours)}: ${firefighter.hours}",
            "\uD83E\uDDD1\u200D\uD83D\uDE92 ${
                when (firefighter.role) {
                    "PRESIDENT" -> stringResource(id = R.string.item_role) + ": " + stringResource(
                        id = R.string.president
                    )

                    "MEMBER" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.member)
                    "ADMIN" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.admin)
                    "USER" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.user)
                    else -> firefighter.role
                }
            }",
        ),
        actions
    )
}