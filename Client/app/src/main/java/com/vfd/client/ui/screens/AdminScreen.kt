package com.vfd.client.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.MainViewModel
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

enum class AdminSection {
    Dashboard,
    Firedepartments
}

@Composable
fun AdminScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    firedepartmentViewModel: FiredepartmentViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val currentUserUiState by userViewModel.currentUserUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    val mainViewModel: MainViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(currentFirefighterUiState.currentFirefighter?.role) {
        mainViewModel.setUserRole(currentFirefighterUiState.currentFirefighter?.role)
    }

    LaunchedEffect(Unit) {
        !currentUserUiState.isLoading
    }

    AppUiEvents(firedepartmentViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()
        firedepartmentViewModel.getFiredepartments()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.AdminScreen -> {
                    userViewModel.getUserByEmailAddress()
                    firefighterViewModel.getFirefighterByEmailAddress()
                    firedepartmentViewModel.getFiredepartments()
                }

                else -> {}
            }
        }
    }

    var section by remember { mutableStateOf(AdminSection.Dashboard) }

    val adminBackFlow = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("admin_back", false)
    val adminBack by (adminBackFlow?.collectAsState() ?: remember { mutableStateOf(false) })

    LaunchedEffect(adminBack) {
        if (adminBack) {
            if (section != AdminSection.Dashboard) {
                section = AdminSection.Dashboard
            } else {
                navController.popBackStack()
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("admin_back", false)
        }
    }

    AppColumn(modifier = Modifier.fillMaxWidth())
    {
        AppLoadingBar(currentUserUiState.isLoading)
        if (!currentUserUiState.isLoading) {
            when (section) {
                AdminSection.Dashboard -> {
                    AppText(
                        "Hello, ${currentUserUiState.currentUser?.firstName.orEmpty()} ${currentUserUiState.currentUser?.lastName.orEmpty()}!",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    DashboardGrid(
                        onOpenFiredepartments = { section = AdminSection.Firedepartments },
                        onLogout = {
                            authViewModel.logout()
                            navController.navigate("welcomeScreen") {
                                popUpTo("moderatorScreen") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                AdminSection.Firedepartments -> {
                    FiredepartmentsHub(
                        firedepartmentViewModel = firedepartmentViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardGrid(
    onOpenFiredepartments: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tiles = listOf(
        Triple("Firedepartments", Icons.Filled.Home, onOpenFiredepartments),
        Triple("Presidents", Icons.Filled.Person, { /* TODO: later */ }),
        Triple("Asset types", Icons.Filled.Edit, { /* TODO: later */ }),
        Triple("Logout", Icons.Filled.ArrowBack, onLogout),
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tiles) { (label, icon, action) ->
            AppButton(
                icon = icon,
                label = label,
                onClick = action,
                fullWidth = true
            )
        }
    }
}