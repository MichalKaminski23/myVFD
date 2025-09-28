package com.vfd.client.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppLoadMore(
    hasMore: Boolean,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        AppButton(
            icon = Icons.AutoMirrored.Filled.Send,
            label = when {
                isLoading -> "Loading..."
                hasMore -> "Load more"
                else -> "No more items"
            },
            onClick = onLoadMore,
            enabled = hasMore && !isLoading,
            fullWidth = true,
        )
    }
}
