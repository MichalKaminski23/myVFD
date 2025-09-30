package com.vfd.client.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun appTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),

        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
        errorIndicatorColor = MaterialTheme.colorScheme.error,

        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
        errorLabelColor = MaterialTheme.colorScheme.error,

        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
        disabledTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),

        cursorColor = MaterialTheme.colorScheme.onPrimary,
        errorCursorColor = MaterialTheme.colorScheme.error
    )
}
