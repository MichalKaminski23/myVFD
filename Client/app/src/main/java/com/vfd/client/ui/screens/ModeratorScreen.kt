package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.vfd.client.ui.components.BaseCard
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {
    val pending by firefighterViewModel.pending.collectAsState()
    val firefighterViewModel: FirefighterViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        firefighterViewModel.loadPendingApplications()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Pending Applications", style = MaterialTheme.typography.titleLarge)

        if (pending.isEmpty()) {
            Text("No pending applications.")
        } else {
            pending.forEach { ff ->
                BaseCard(
                    "👤 ${ff.firstName}",
                    "📧 ID: ${ff.userId}",
                    "🚒 Firedepartment: ${ff.firedepartmentName}",
                    null
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            firefighterViewModel.changeFirefighterRoleOrStatus(
                                firefighterId = ff.firefighterId,
                                patch = FirefighterDtos.FirefighterPatch(
                                    role = Role.MEMBER,
                                    status = FirefighterStatus.ACTIVE,
                                )
                            )
                            firefighterViewModel.loadPendingApplications()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Approve")
                    }
                    Button(
                        onClick = {
                            firefighterViewModel.changeFirefighterRoleOrStatus(
                                firefighterId = ff.firefighterId,
                                patch = FirefighterDtos.FirefighterPatch(
                                    role = Role.USER,
                                    status = FirefighterStatus.PENDING,
                                )
                            )
                            firefighterViewModel.loadPendingApplications()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}
