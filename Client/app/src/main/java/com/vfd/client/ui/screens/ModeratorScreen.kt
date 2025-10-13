package com.vfd.client.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterCard
import com.vfd.client.ui.components.cards.AppUserCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.MainViewModel
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val currentUserUiState by userViewModel.currentUserUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    AppUiEvents(firefighterViewModel.uiEvents, snackbarHostState)

    var showInputs by remember { mutableStateOf(false) }
    var quarterInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }

    val mainViewModel: MainViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(currentFirefighterUiState.currentFirefighter?.role) {
        mainViewModel.setUserRole(currentFirefighterUiState.currentFirefighter?.role)
    }

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.ModeratorScreen -> {
                    userViewModel.getUserByEmailAddress()
                    firefighterViewModel.getFirefighterByEmailAddress()
                }

                else -> {}
            }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (currentUserUiState.isLoading) {
            AppText(
                "The data is loading...",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        currentUserUiState.currentUser?.let { user ->
            AppUserCard(user)
        }

        currentFirefighterUiState.currentFirefighter?.let { firefighter ->
            AppFirefighterCard(
                firefighter,
                quarterHours = currentFirefighterUiState.hours?.hours,
                actions = {
                    if (showInputs) {
                        AppTextField(
                            value = quarterInput,
                            onValueChange = { raw ->
                                val digits = raw.filter(Char::isDigit).take(1)
                                if (digits.isEmpty() || digits in listOf("1", "2", "3", "4")) {
                                    quarterInput = digits
                                }
                            },
                            label = "Quarter (1-4)",
                            errorMessage = currentFirefighterUiState.errorMessage,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        AppTextField(
                            value = yearInput,
                            onValueChange = { raw ->
                                yearInput = raw.filter(Char::isDigit).take(4)
                            },
                            label = "Year (YYYY)",
                            errorMessage = currentFirefighterUiState.errorMessage,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        AppButton(
                            icon = Icons.Default.ThumbUp,
                            label = "Show hours from quarter",
                            onClick = {
                                val quarter = quarterInput.toIntOrNull() ?: 0
                                val year = yearInput.toIntOrNull() ?: 0
                                firefighterViewModel.getHoursForQuarter(year, quarter)
                                showInputs = false
                            },
                            enabled = quarterInput.isNotEmpty() && yearInput.isNotEmpty()
                        )
                    }
                    if (!showInputs) {
                        AppButton(
                            icon = Icons.Default.ThumbUp,
                            label = "Show hours from quarter",
                            onClick = { showInputs = true }
                        )
                    }
                }
            )
        }

        AppLoadingBar(currentUserUiState.isLoading)
        AppLoadingBar(currentFirefighterUiState.isLoading)

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