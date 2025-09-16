package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.data.remote.dtos.Role
import com.vfd.client.ui.components.ActionButton
import com.vfd.client.ui.components.BaseCard
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {
    val pendingFirefighters by firefighterViewModel.pending.collectAsState()

    LaunchedEffect(Unit) {
        firefighterViewModel.loadPendingApplications()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Pending Applications", style = MaterialTheme.typography.titleLarge)

        if (pendingFirefighters.isEmpty()) {
            Text("No pending applications.")
        } else {
            pendingFirefighters.forEach { firefighter ->
                BaseCard(
                    "👤 ${firefighter.firstName} ${firefighter.lastName}",
                    "📧 ID: ${firefighter.userId}",
                    "🚒 Firedepartment: ${firefighter.firedepartmentName}",
                    null
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ActionButton(
                        icon = Icons.Default.Add,
                        label = "Approve",
                        onClick = {
                            firefighterViewModel.changeFirefighterRoleOrStatus(
                                firefighterId = firefighter.firefighterId,
                                firefighterDto = FirefighterDtos.FirefighterPatch(
                                    role = Role.MEMBER,
                                    status = FirefighterStatus.ACTIVE,
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 120.dp)
                    )
                    ActionButton(
                        icon = Icons.Default.Delete,
                        label = "Reject",
                        onClick = {
                            firefighterViewModel.changeFirefighterRoleOrStatus(
                                firefighterId = firefighter.firefighterId,
                                firefighterDto = FirefighterDtos.FirefighterPatch(
                                    role = Role.USER,
                                    status = FirefighterStatus.REJECTED,
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 120.dp)
                    )
                }
            }
        }
    }
}