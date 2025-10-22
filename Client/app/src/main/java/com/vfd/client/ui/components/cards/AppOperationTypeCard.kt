package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.OperationTypeDtos

@Composable
fun AppOperationTypeCard(
    operationType: OperationTypeDtos.OperationTypeResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83D\uDDC2\uFE0F ${operationType.operationType}",
            "\uD83E\uDEC0 ${operationType.name}"
        ),
        actions
    )
}