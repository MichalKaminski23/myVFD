package com.vfd.client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class NavBarButton(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val selected: Boolean = false,
    val badgeCount: Int? = null
)

@Composable
fun NavBarAction(
    actions: List<NavBarButton>
) {
    if (actions.size == 1) {
        NavigationBar {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AppColumn(
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = actions[0].onClick,
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                if (actions[0].badgeCount != null && actions[0].badgeCount!! > 0) {
                                    androidx.compose.material3.Badge {
                                        Text(actions[0].badgeCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = actions[0].icon,
                                contentDescription = actions[0].label,
                            )
                        }
                    }
                    Spacer(Modifier.height(7.dp))
                    Text(
                        text = actions[0].label,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    } else {
        NavigationBar {
            actions.forEach { action ->
                NavigationBarItem(
                    selected = action.selected,
                    onClick = action.onClick,
                    icon = {
                        BadgedBox(
                            badge = {
                                if (action.badgeCount != null && action.badgeCount > 0) {
                                    androidx.compose.material3.Badge {
                                        Text(action.badgeCount.toString())
                                    }
                                }
                            }

                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(action.icon, contentDescription = action.label)
                            }
                        }
                    },
                    label = {
                        Text(
                            action.label,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}