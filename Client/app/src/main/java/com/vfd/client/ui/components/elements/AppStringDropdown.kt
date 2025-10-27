package com.vfd.client.ui.components.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vfd.client.R
import com.vfd.client.ui.theme.appTextFieldColors

@Composable
fun AppStringDropdown(
    label: String,
    items: List<String>,
    selected: String?,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }
    var menuWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val shape = RoundedCornerShape(14.dp)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selected ?: "",
            onValueChange = { /* readOnly */ },
            readOnly = true,
            enabled = enabled,
            label = {
                Text(
                    label,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            placeholder = { stringResource(id = R.string.select) },
            trailingIcon = {
                IconButton(onClick = { if (enabled) expanded = !expanded }) {
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
                }
                .clickable(enabled = enabled) { expanded = true },
            colors = appTextFieldColors()
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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                IconButton(onClick = { expanded = false }) { Icon(Icons.Default.Close, null) }
            }
            AppHorizontalDivider()

            if (items.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                stringResource(id = R.string.no_more_items),
                                textAlign = TextAlign.Center,
                                color = itemTextColor
                            )
                        }
                    },
                    onClick = { /* no-op */ },
                    enabled = false,
                )
            } else {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, color = itemTextColor) },
                        onClick = {
                            onSelected(item)
                            expanded = true
                        },
                        leadingIcon = {
                            Icon(
                                leadingIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = itemTextColor,
                            leadingIconColor = itemTextColor,
                            trailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}