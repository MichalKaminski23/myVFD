package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.AssetDtos

@Composable
fun AppAssetCard(
    asset: AssetDtos.AssetResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83E\uDE93 ${asset.name}",
            "\uD83D\uDEA8 Type: ${asset.assetTypeName}",
            "✏\uFE0F Description: ${asset.description}"
        ),
        actions = actions
    )
}