package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.InspectionDtos

@Composable
fun AppInspectionCard(
    inspection: InspectionDtos.InspectionResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83D\uDD75 ${inspection?.inspectionTypeName}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_date)}: ${inspection?.inspectionDate}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_end_date)}: ${
                if (inspection?.expirationDate == null) {
                    stringResource(id = R.string.not_set)
                } else inspection.expirationDate
            }"
        ),
        actions = actions
    )
}