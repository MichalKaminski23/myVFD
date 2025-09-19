package com.vfd.client.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> AppDropdown(
    items: List<T>,
    selectedId: Int? = null,
    selectedCode: String? = null,
    idSelector: ((T) -> Int)? = null,
    codeSelector: ((T) -> String)? = null,
    labelSelector: (T) -> String,
    label: String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    hasMore: Boolean = false,
    onExpand: (() -> Unit)? = null,
    icon: ImageVector,
    redBackground: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }

    val selected: T? = when {
        selectedId != null && idSelector != null ->
            items.firstOrNull { idSelector(it) == selectedId }

        !selectedCode.isNullOrBlank() && codeSelector != null ->
            items.firstOrNull { codeSelector(it) == selectedCode }

        else -> null
    }

    LaunchedEffect(expanded) {
        if (expanded && !initialized) {
            onExpand?.invoke()
            initialized = true
        }
    }

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
    }

    if (redBackground) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected?.let(labelSelector) ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            placeholder = { Text("Select...") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                },
            colors = colors,
        )

        val menuBg =
            if (redBackground) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        val menuBorder = if (redBackground) Color.White else MaterialTheme.colorScheme.onPrimary
        val itemTextColor = if (redBackground) Color.White else MaterialTheme.colorScheme.onPrimary
        val itemIconTint = itemTextColor

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .heightIn(max = 300.dp)
                .background(MaterialTheme.colorScheme.primary)
                .background(menuBg)
                .border(BorderStroke(1.dp, menuBorder)),
            shape = MaterialTheme.shapes.medium,
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = { Icon(icon, contentDescription = null, tint = itemIconTint) },

                    text = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(labelSelector(item), textAlign = TextAlign.Center)
                        }
                    },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }

            if (hasMore) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Load more...", textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    onClick = { onLoadMore() }
                )
            } else {
                DropdownMenuItem(
                    text = {
                        Text(
                            "No more items...", textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    onClick = { /* No-op */ }
                )
            }
        }
    }
}
