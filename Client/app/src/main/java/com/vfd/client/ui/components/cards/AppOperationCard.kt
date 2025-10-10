package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.OperationDtos

@Composable
fun AppOperationCard(
    operation: OperationDtos.OperationResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83E\uDD1D ${operation?.operationTypeName}",
            "\uD83D\uDCC6 Date: ${operation?.operationDate}",
            "✏\uFE0F Description: ${operation?.description}",
            "🏠 ${operation?.address?.country}, ${operation?.address?.voivodeship}, " +
                    "${operation?.address?.street} ${operation?.address?.houseNumber}/${operation?.address?.apartNumber ?: ""} " +
                    "${operation?.address?.postalCode} ${operation?.address?.city}",
            "👩‍🚒 Participants: \n ${operation?.participants?.joinToString { "${it.firstName} ${it.lastName}" }}"
        ),
        actions = actions
    )
}