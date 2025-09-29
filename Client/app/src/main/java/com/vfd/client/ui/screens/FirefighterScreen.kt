package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppLoadMoreButton
import com.vfd.client.ui.components.cards.AppFirefightersCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
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

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (activeFirefightersUiState.activeFirefighters.isEmpty()) {
            AppText(
                "There aren't any firefighters in your VFD or the firefighters are still loading",
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            activeFirefightersUiState.activeFirefighters.forEach { firefighter ->
                AppFirefightersCard(
                    firefighter = firefighter, actions =
                        {
//                            AppButton(
//                                icon = Icons.Default.Delete,
//                                label = "Deactivate",
//                                onClick = {
//                                    firefighterViewModel.changeFirefighterRoleOrStatus(
//                                        firefighterId = firefighter.firefighterId,
//                                        firefighterDto = FirefighterDtos.FirefighterPatch(
//                                            role = FirefighterRole.USER.toString(),
//                                            status = FirefighterStatus.REJECTED.toString(),
//                                        )
//                                    )
//                                },
//                            )
                        })
            }
        }

        Spacer(Modifier.height(12.dp))
        AppLoadMoreButton(
            hasMore = hasMore,
            isLoading = activeFirefightersUiState.isLoading,
            onLoadMore = {
                if (hasMore && !activeFirefightersUiState.isLoading)
                    firefighterViewModel.getFirefighters(page = activeFirefightersUiState.page + 1)
            }
        )
    }
}