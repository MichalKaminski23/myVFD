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

        focusedIndicatorColor = colors.onPrimary,
        unfocusedIndicatorColor = colors.onPrimary.copy(alpha = 0.7f),
        errorIndicatorColor = colors.error,

        focusedLabelColor = colors.onPrimary,
        unfocusedLabelColor = colors.onPrimary.copy(alpha = 0.8f),
        errorLabelColor = colors.error,

        focusedTextColor = colors.onPrimary,
        unfocusedTextColor = colors.onPrimary,
        disabledTextColor = colors.onSurface.copy(alpha = 0.5f),

        cursorColor = colors.onPrimary,
        errorCursorColor = colors.error
    )
}
