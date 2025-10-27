package com.vfd.client.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vfd.client.R

@Composable
fun AppLoadMoreButton(
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
                isLoading -> stringResource(id = R.string.loading)
                hasMore -> stringResource(id = R.string.load_more)
                else -> stringResource(id = R.string.no_more_items)
            },
            onClick = onLoadMore,
            enabled = hasMore && !isLoading,
            fullWidth = true,
        )
    }
}
