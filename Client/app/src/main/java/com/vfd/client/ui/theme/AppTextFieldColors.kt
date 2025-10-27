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
        focusedContainerColor = colors.primary.copy(alpha = 0.06f),
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = colors.surface.copy(alpha = 0.5f),

        focusedIndicatorColor = colors.onSurface,
        unfocusedIndicatorColor = colors.onSurface.copy(alpha = 0.6f),
        errorIndicatorColor = colors.error,

        focusedLabelColor = colors.onSurface,
        unfocusedLabelColor = colors.onSurface.copy(alpha = 0.8f),
        errorLabelColor = colors.error,

        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        disabledTextColor = colors.onSurface.copy(alpha = 0.5f),

        cursorColor = colors.onSurface,
        errorCursorColor = colors.error
    )
}
