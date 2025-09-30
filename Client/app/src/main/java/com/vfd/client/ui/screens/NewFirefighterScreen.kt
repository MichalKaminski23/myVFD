package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.buttons.AppLoadMoreButton
import com.vfd.client.ui.components.cards.AppFirefightersCard
import com.vfd.client.ui.components.elements.AppSearchBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun NewFirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val pendingFirefightersUiState by firefighterViewModel.pendingFirefightersUiState.collectAsState()
    val hasMore = pendingFirefightersUiState.page + 1 < pendingFirefightersUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    val filteredFirefighters = pendingFirefightersUiState.pendingFirefighters.filter {
        val fullName = "${it.firstName} ${it.lastName}"
        searchQuery.isBlank() ||
                it.firstName.contains(searchQuery, ignoreCase = true) ||
                it.lastName.contains(searchQuery, ignoreCase = true) ||
                fullName.contains(searchQuery, ignoreCase = true) ||
                it.emailAddress.contains(searchQuery, ignoreCase = true)
    }

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

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search firefighters...",
                enabled = !pendingFirefightersUiState.isLoading,
                loading = pendingFirefightersUiState.isLoading,
            )
            Spacer(Modifier.height(12.dp))
        }

        if (filteredFirefighters.isEmpty()) {
            item {
                AppText(
                    if (searchQuery.isBlank())
                        "There aren't any firefighters in your VFD or the firefighters are still loading"
                    else
                        "No firefighters match your search",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        } else {
            items(filteredFirefighters) { firefighter ->
                AppFirefightersCard(
                    firefighter = firefighter,
                    actions = {
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
                Spacer(Modifier.height(12.dp))
            }
        }

        item {
            Spacer(Modifier.height(12.dp))
            AppLoadMoreButton(
                hasMore = hasMore,
                isLoading = pendingFirefightersUiState.isLoading,
                onLoadMore = {
                    if (hasMore && !pendingFirefightersUiState.isLoading) {
                        firefighterViewModel.getPendingFirefighters(
                            page = pendingFirefightersUiState.page + 1
                        )
                    }
                }
            )
        }
    }
}
