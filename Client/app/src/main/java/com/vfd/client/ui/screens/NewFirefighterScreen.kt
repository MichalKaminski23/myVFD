package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefightersCard
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun NewFirefighterScreen(
    firefighterViewModel: FirefighterViewModel,
    snackbarHostState: SnackbarHostState
) {
    val pendingFirefightersUiState by firefighterViewModel.pendingFirefightersUiState.collectAsState()
    val hasMore = pendingFirefightersUiState.page + 1 < pendingFirefightersUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(firefighterViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(Unit) {
        firefighterViewModel.getPendingFirefighters(page = 0, refresh = true)

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.NewFirefighterScreen ->
                    firefighterViewModel.getPendingFirefighters(page = 0, refresh = true)

                else -> {}
            }
        }
    }

    AppListScreen(
        data = pendingFirefightersUiState.pendingFirefighters,
        isLoading = pendingFirefightersUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        filter = { firefighter, query ->
            val fullName = "${firefighter.firstName} ${firefighter.lastName}"
            query.isBlank() ||
                    firefighter.firstName.contains(query, ignoreCase = true) ||
                    firefighter.lastName.contains(query, ignoreCase = true) ||
                    fullName.contains(query, ignoreCase = true) ||
                    firefighter.emailAddress.contains(query, ignoreCase = true)
        },
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !pendingFirefightersUiState.isLoading) {
                firefighterViewModel.getPendingFirefighters(
                    page = pendingFirefightersUiState.page + 1
                )
            }
        },
        errorMessage = pendingFirefightersUiState.errorMessage,
        itemKey = { it.firefighterId },
    ) { firefighter ->
        AppFirefightersCard(
            firefighter = firefighter,
            actions = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(
                        icon = Icons.Default.Add,
                        label = stringResource(id = R.string.accept),
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
                        label = stringResource(id = R.string.reject),
                        onClick = {
                            firefighterViewModel.deleteFirefighter(
                                firefighterId = firefighter.firefighterId
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        )
    }
}