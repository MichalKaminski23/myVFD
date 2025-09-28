package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppCard
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppLoadMore
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun NewFirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {
    val pendingFirefighters by firefighterViewModel.pendingFirefightersUiState.collectAsState()

    val hasMore = pendingFirefighters.page + 1 < pendingFirefighters.totalPages

    LaunchedEffect(Unit) {
        firefighterViewModel.getPendingFirefighters()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        if (pendingFirefighters.pendingFirefighters.isEmpty()) {
            Text(
                "No pending applications",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            pendingFirefighters.pendingFirefighters.forEach { firefighter ->
                AppCard(
                    "👤 ${firefighter.firstName} ${firefighter.lastName}",
                    "🚒 Firedepartment: ${firefighter.firedepartmentName}",
                    "📧 Email address: ${firefighter.emailAddress}",
                    {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Add,
                                label = "Approve",
                                onClick = {
                                    firefighterViewModel.changeFirefighterRoleOrStatus(
                                        firefighterId = firefighter.firefighterId,
                                        firefighterDto = FirefighterDtos.FirefighterPatch(
                                            role = FirefighterRole.MEMBER.toString(),
                                            status = FirefighterStatus.ACTIVE.toString(),
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            AppButton(
                                icon = Icons.Default.Delete,
                                label = "Reject",
                                onClick = {
                                    firefighterViewModel.changeFirefighterRoleOrStatus(
                                        firefighterId = firefighter.firefighterId,
                                        firefighterDto = FirefighterDtos.FirefighterPatch(
                                            role = FirefighterRole.USER.toString(),
                                            status = FirefighterStatus.REJECTED.toString(),
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        AppLoadMore(
            hasMore = hasMore,
            isLoading = pendingFirefighters.isLoading,
            onLoadMore = {
                if (hasMore && !pendingFirefighters.isLoading) {
                    firefighterViewModel.getPendingFirefighters(
                        page = pendingFirefighters.page + 1
                    )
                }
            }
        )
    }
}