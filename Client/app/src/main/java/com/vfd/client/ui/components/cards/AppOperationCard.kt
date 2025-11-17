package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.OperationDtos

@Composable
fun AppOperationCard(
    operation: OperationDtos.OperationResponse?,
    actions: @Composable (() -> Unit)? = null
) {
    AppCard(
        listOf(
            "\uD83E\uDD1D ${operation?.operationTypeName}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_date)}: ${operation?.operationDate}",
            "\uD83D\uDCC6 ${stringResource(id = R.string.item_end_date)}: ${operation?.operationEnd}",
            "✏\uFE0F ${stringResource(id = R.string.item_description)}: ${operation?.description}",
            "🏠 ${operation?.address?.country}, ${operation?.address?.voivodeship}, " +
                    "${operation?.address?.street} ${operation?.address?.houseNumber}/${operation?.address?.apartNumber ?: ""} " +
                    "${operation?.address?.postalCode} ${operation?.address?.city}",
            "👩‍🚒 ${stringResource(id = R.string.firefighters)}: \n ${operation?.participants?.joinToString { "${it.firstName} ${it.lastName}" }}"
        ),
        actions = actions
    )
}