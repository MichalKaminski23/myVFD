package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FiredepartmentDtos

@Composable
fun AppFiredepartmentCard(
    firedepartment: FiredepartmentDtos.FiredepartmentResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "🚒 ${firedepartment.name}",
            "🏠 ${firedepartment.address.country}, ${firedepartment.address.voivodeship}, " +
                    "${firedepartment.address.street} ${firedepartment.address.houseNumber}/${firedepartment.address.apartNumber ?: ""} " +
                    "${firedepartment.address.postalCode} ${firedepartment.address.city}",
            "⛑\uFE0F ${stringResource(id = R.string.item_nrfs)}: ${
                if (firedepartment.nrfs) stringResource(
                    id = R.string.yes
                ) else stringResource(id = R.string.no)
            }"
        ),
        actions
    )
}