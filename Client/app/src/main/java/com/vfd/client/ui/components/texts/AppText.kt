package com.vfd.client.ui.components.texts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppText(
    text: String,
    color: Color = Color.Unspecified,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    style: TextStyle
) {
    Text(
        text,
        color = color,
        textAlign = TextAlign.Center,
        style = style,
        modifier = modifier.fillMaxWidth()
    )
}