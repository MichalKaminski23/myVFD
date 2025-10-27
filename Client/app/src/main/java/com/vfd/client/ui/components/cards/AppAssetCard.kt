package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.AssetDtos

@Composable
fun AppAssetCard(
    asset: AssetDtos.AssetResponse?,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83E\uDE93 ${asset?.name}",
            "\uD83D\uDEA8 ${stringResource(id = R.string.item_type)}: ${asset?.assetTypeName}",
            "✏\uFE0F ${stringResource(id = R.string.item_description)}: ${asset?.description}"
        ),
        actions = actions
    )
}