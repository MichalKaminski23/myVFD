package com.vfd.client.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.cards.AppFirefightersCard
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun FirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val activeFirefightersUiState by firefighterViewModel.activeFirefightersUiState.collectAsState()
    val hasMore = activeFirefightersUiState.page + 1 < activeFirefightersUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    AppUiEvents(firefighterViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(Unit) {
        firefighterViewModel.getFirefighters(page = 0, refresh = true)

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.FirefighterScreen -> firefighterViewModel.getFirefighters(
                    page = 0,
                    refresh = true
                )

                else -> {}
            }
        }
    }

    AppListScreen(
        data = activeFirefightersUiState.activeFirefighters,
        isLoading = activeFirefightersUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        searchPlaceholder = "Search firefighters...",
        filter = { firefighter, query ->
            val fullName = "${firefighter.firstName} ${firefighter.lastName}"
            query.isBlank() ||
                    firefighter.firstName.contains(query, ignoreCase = true) ||
                    firefighter.lastName.contains(query, ignoreCase = true) ||
                    fullName.contains(query, ignoreCase = true) ||
                    firefighter.emailAddress.contains(query, ignoreCase = true)
        },
        emptyText = "There aren't any firefighters in your VFD or the firefighters are still loading",
        emptyFilteredText = "No firefighters match your search",
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !activeFirefightersUiState.isLoading)
                firefighterViewModel.getFirefighters(page = activeFirefightersUiState.page + 1)
        },
        errorMessage = activeFirefightersUiState.errorMessage,
        itemKey = { it.firefighterId }
    ) { firefighter ->
        AppFirefightersCard(
            firefighter = firefighter,
            actions = {
            }
        )
    }
}