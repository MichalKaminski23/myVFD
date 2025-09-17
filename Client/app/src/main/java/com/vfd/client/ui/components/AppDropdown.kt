package com.vfd.client.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> AppDropdown(
    items: List<T>,
    selectedId: Int?,
    idSelector: (T) -> Int,
    labelSelector: (T) -> String,
    label: String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    hasMore: Boolean = false,
    onExpand: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }
    val selected = items.firstOrNull { idSelector(it) == selectedId }

    LaunchedEffect(expanded) {
        if (expanded && !initialized) {
            onExpand?.invoke()
            initialized = true
        }
    }

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
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(345.dp)
                .heightIn(max = 300.dp)
                .background(MaterialTheme.colorScheme.primary)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary))
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
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
