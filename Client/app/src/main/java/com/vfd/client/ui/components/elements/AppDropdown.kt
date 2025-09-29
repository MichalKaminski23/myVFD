package com.vfd.client.ui.components.elements

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    hasMore: Boolean = false,
    onExpand: (() -> Unit)? = null,
    icon: ImageVector,
    isLoading: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var initialized by remember { mutableStateOf(false) }
    var menuWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

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

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,

        focusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
        errorIndicatorColor = MaterialTheme.colorScheme.error,

        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorLabelColor = MaterialTheme.colorScheme.error,

        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),

        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error
    )

    val shape = RoundedCornerShape(14.dp)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected?.let(labelSelector) ?: "",
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    label,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = { Text("Select...") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            shape = shape,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coords ->
                    menuWidth = with(density) { coords.size.width.toDp() }
                },
            colors = fieldColors
        )

        val menuElevation = 8.dp
        val menuBackground = MaterialTheme.colorScheme.surfaceColorAtElevation(menuElevation)
        val menuBorder = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
        val itemTextColor = MaterialTheme.colorScheme.onSurface

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(menuWidth)
                .heightIn(max = 320.dp)
                .clip(shape)
                .border(BorderStroke(1.dp, menuBorder), shape)
                .background(menuBackground),
            border = BorderStroke(1.dp, Color.White),
            shape = shape
        ) {
            if (isLoading) {
                DropdownMenuItem(
                    text = {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    onClick = { /* no-op while loading */ }
                )
            } else if (items.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    onClick = { /* no-op */ },
                    enabled = false
                )
            } else {
                items.forEach { item ->
                    val isSelected =
                        selected != null && labelSelector(item) == labelSelector(selected)
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )
                        },
                        trailingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        text = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    labelSelector(item),
                                    textAlign = TextAlign.Center,
                                    color = itemTextColor
                                )
                            }
                        },
                        onClick = {
                            onSelected(item)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = itemTextColor,
                            leadingIconColor = itemTextColor,
                            trailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                AppHorizontalDivider(color = menuBorder)

                if (hasMore) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Load more...",
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic,
                                color = itemTextColor
                            )
                        },
                        onClick = { onLoadMore() },
                        colors = MenuDefaults.itemColors(textColor = itemTextColor)
                    )
                } else {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "No more items...",
                                textAlign = TextAlign.Center,
                                fontStyle = FontStyle.Italic,
                                color = itemTextColor.copy(alpha = 0.8f)
                            )
                        },
                        onClick = { /* no-op */ },
                        enabled = false,
                        colors = MenuDefaults.itemColors(textColor = itemTextColor)
                    )
                }
            }
        }
    }
}
