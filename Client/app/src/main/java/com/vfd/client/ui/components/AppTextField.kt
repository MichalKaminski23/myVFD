package com.vfd.client.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    redBackground: Boolean = false
) {
    val colors = if (redBackground) {
        TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            unfocusedContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),

            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.8f),
            errorIndicatorColor = Color.White,

            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            errorLabelColor = Color.White,

            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White.copy(alpha = 0.7f),

            cursorColor = Color.White,
            errorCursorColor = Color.White
        )
    } else {
        TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,

            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            errorIndicatorColor = MaterialTheme.colorScheme.error,

            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLabelColor = MaterialTheme.colorScheme.error,

            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),

            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error
        )
    }

    AppColumn(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            colors = colors
        )
        if (errorMessage != null) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}