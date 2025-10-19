package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
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
            "⛑\uFE0F NRFS: ${if (firedepartment.nrfs) "Yes" else "No"}"
        ),
        actions
    )
}