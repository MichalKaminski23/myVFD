package com.vfd.client.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun appTextFieldColors(): TextFieldColors {
    val colors = MaterialTheme.colorScheme
    return TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = colors.surface.copy(alpha = 0.5f),

        focusedIndicatorColor = colors.primary,
        unfocusedIndicatorColor = colors.outline,
        errorIndicatorColor = colors.error,

        focusedLabelColor = colors.onSurface,
        unfocusedLabelColor = colors.onSurface.copy(alpha = 0.7f),
        errorLabelColor = colors.error,

        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        disabledTextColor = colors.onSurface.copy(alpha = 0.5f),

        cursorColor = colors.primary,
        errorCursorColor = colors.error
    )
}
