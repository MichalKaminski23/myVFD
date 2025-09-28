package com.vfd.client.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AppErrorText(message: String) {
    Text(
        message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(8.dp),
        textAlign = TextAlign.Center
    )
}