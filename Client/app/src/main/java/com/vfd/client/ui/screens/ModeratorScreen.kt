package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterCard
import com.vfd.client.ui.components.cards.AppUserCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.UserViewModel

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentUserUiState by userViewModel.currentUserUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        AppLoadingBar(currentUserUiState.isLoading)
        AppLoadingBar(currentFirefighterUiState.isLoading)

        currentUserUiState.currentUser?.let { user ->
            AppUserCard(user)
        }

        currentFirefighterUiState.currentFirefighter?.let { firefighter ->
            AppFirefighterCard(firefighter)
        }

        Spacer(Modifier.height(24.dp))
        AppButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            label = "Logout",
            onClick = {
                authViewModel.logout()
                navController.navigate("welcomeScreen") {
                    popUpTo("moderatorScreen") { inclusive = true }
                }
            },
            fullWidth = true,
        )
    }
}