package com.vfd.client.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
) {
    Spacer(Modifier.padding(top = 16.dp))
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
    Spacer(Modifier.padding(top = 16.dp))
}