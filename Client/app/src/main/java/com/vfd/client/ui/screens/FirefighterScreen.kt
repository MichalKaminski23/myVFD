package com.vfd.client.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefightersCard
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.viewmodels.FirefighterActivityViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager
import com.vfd.client.utils.daysUntilSomething
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    firefighterActivityViewModel: FirefighterActivityViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val activeFirefightersUiState by firefighterViewModel.activeFirefightersUiState.collectAsState()
    val hasMore = activeFirefightersUiState.page + 1 < activeFirefightersUiState.totalPages

    var searchQuery by remember { mutableStateOf("") }

    val firefighterActivityUiState by firefighterActivityViewModel.firefighterActivityUiState.collectAsState()
    val expiringCounts = remember { mutableStateMapOf<Int, Int>() }

    LaunchedEffect(firefighterActivityUiState.activities) {
        val map = firefighterActivityUiState.activities
            .groupBy { it.firefighterActivityId }
            .mapValues { (_, list) ->
                list.count { dto ->
                    val days = dto.expirationDate?.toString()
                        ?.let { daysUntilSomething(it.toLocalDateTime()) } ?: -1
                    days in 0..30
                }
            }
        expiringCounts.clear()
        expiringCounts.putAll(map)
    }

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
                val count = expiringCounts[firefighter.firefighterId] ?: 0
                BadgedBox(
                    badge = {
                        if (count > 0) {
                            Badge { Text("$count") }
                        }
                    }
                ) {
                    AppButton(
                        icon = Icons.Default.Warning,
                        label = "Activities",
                        onClick = {
                            //val encodedName = Uri.encode(firefighter.name)
                            //navController.navigate("activities/list?firefighterId=${firefighter.firefighterId}&assetName=$encodedName")
                            navController.navigate("activities/list?firefighterId=${firefighter.firefighterId}")
                        }
                    )
                }
            }
        )
    }
}