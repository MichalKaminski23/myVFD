package com.vfd.client.ui.components.cards

import androidx.compose.runtime.Composable
import com.vfd.client.data.remote.dtos.AssetTypeDtos

@Composable
fun AppAssetTypeCard(
    assetType: AssetTypeDtos.AssetTypeResponse,
    actions: @Composable (() -> Unit)? = null,
) {
    AppCard(
        listOf(
            "\uD83D\uDDC2\uFE0F ${assetType.assetType}",
            "🛠️ ${assetType.name}"
        ),
        actions
    )
}