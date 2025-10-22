package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.InspectionTypeDtos

@Composable
fun AppInspectionTypeCard(
    inspectionType: InspectionTypeDtos.InspectionTypeResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83D\uDDC2\uFE0F ${inspectionType.inspectionType}",
            "\uD83D\uDD0E ${inspectionType.name}"
        ),
        actions
    )
}