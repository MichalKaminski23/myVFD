package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.InspectionDtos

@Composable
fun AppInspectionCard(
    inspection: InspectionDtos.InspectionResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDD75 ${inspection?.inspectionTypeName}",
            "\uD83D\uDCC6 Inspection date: ${inspection?.inspectionDate}",
            "\uD83D\uDCC6 Expiration date: ${if (inspection?.expirationDate == null) "Not set" else inspection.expirationDate}"
        ),
        actions = actions
    )
}