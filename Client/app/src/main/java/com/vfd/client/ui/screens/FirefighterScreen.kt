package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun FirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {

    val activeFirefightersUiState by firefighterViewModel.activeFirefightersUiState.collectAsState()

    val hasMore = activeFirefightersUiState.page + 1 < activeFirefightersUiState.totalPages

    LaunchedEffect(Unit) {
        firefighterViewModel.getFirefighters()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (activeFirefightersUiState.activeFirefighters.isEmpty()) {
            AppText(
                "There aren't any firefighters in your VFD",
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            activeFirefightersUiState.activeFirefighters.forEach { firefighter ->
                AppFirefightersCard(firefighter = firefighter)
            }
        }

        Spacer(Modifier.height(12.dp))
        AppLoadMoreButton(
            hasMore = hasMore,
            isLoading = activeFirefightersUiState.isLoading,
            onLoadMore = {
                if (hasMore && !activeFirefightersUiState.isLoading) firefighterViewModel.getFirefighters(
                    page = activeFirefightersUiState.page + 1
                )
            }
        )
    }
}