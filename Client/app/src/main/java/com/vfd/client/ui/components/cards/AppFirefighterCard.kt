package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.data.remote.dtos.FirefighterDtos

@Composable
fun AppFirefighterCard(
    firefighter: FirefighterDtos.FirefighterResponse,
    firedepartment: FiredepartmentDtos.FiredepartmentResponse,
    quarterHours: Double? = null,
    actions: @Composable (() -> Unit)? = null,
) {
    val cardTexts = mutableListOf(
        "\uD83D\uDE92 ${firefighter.firedepartmentName}",
        "🏠 ${firedepartment.address.country}, ${firedepartment.address.voivodeship}, " +
                "${firedepartment.address.street} ${firedepartment.address.houseNumber}/${firedepartment.address.apartNumber ?: ""} " +
                "${firedepartment.address.postalCode} ${firedepartment.address.city}",
        "⛑\uFE0F ${stringResource(id = R.string.item_nrfs)}: ${
            if (firedepartment.nrfs) {
                stringResource(id = R.string.yes)
            } else {
                stringResource(id = R.string.no)
            }
        }",
        "\uD83E\uDDD1\u200D\uD83D\uDE92 ${
            when (firefighter.role) {
                "PRESIDENT" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.president)
                "MEMBER" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.member)
                "ADMIN" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.admin)
                "USER" -> stringResource(id = R.string.item_role) + ": " + stringResource(id = R.string.user)
                else -> firefighter.role
            }
        }",
        "⌚ ${stringResource(id = R.string.hours)}: ${firefighter.hours}"
    )
    quarterHours?.let {
        cardTexts.add("🗓 ${stringResource(id = R.string.hours)}: $it")
    }
    AppCard(cardTexts, actions)
}