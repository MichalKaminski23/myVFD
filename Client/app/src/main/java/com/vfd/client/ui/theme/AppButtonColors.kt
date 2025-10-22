package com.vfd.client.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun appButtonColors(): ButtonColors {
    val colors = MaterialTheme.colorScheme
    return ButtonDefaults.buttonColors(
        containerColor = colors.primary,
        contentColor = colors.onPrimary,
        disabledContainerColor = colors.primary.copy(alpha = 0.5f),
        disabledContentColor = colors.onPrimary.copy(alpha = 0.5f)
    )
}
