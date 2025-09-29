package com.vfd.client.ui.components.cards

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText

@Composable
fun AppCard(
    texts: List<String>,
    actions: @Composable (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        AppColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            texts.forEachIndexed { index, text ->
                val style = when (index) {
                    0 -> MaterialTheme.typography.titleLarge
                    else -> MaterialTheme.typography.bodyMedium
                }
                AppText(text, style = style)
            }

            actions?.let {
                Spacer(modifier = Modifier.height(12.dp))
                it()
            }
        }
    }
}