package com.vfd.client.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.R
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.viewmodels.AssetTypeViewModel
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel
import com.vfd.client.ui.viewmodels.FirefighterActivityTypeViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.InspectionTypeViewModel
import com.vfd.client.ui.viewmodels.MainViewModel
import com.vfd.client.ui.viewmodels.OperationTypeViewModel
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

enum class AdminSection {
    Dashboard,
    Firedepartments,
    Presidents,
    AssetTypes,
    FirefighterActivityTypes,
    InspectionTypes,
    OperationTypes
}

@Composable
fun AdminScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    firedepartmentViewModel: FiredepartmentViewModel = hiltViewModel(),
    assetTypeViewModel: AssetTypeViewModel = hiltViewModel(),
    firefighterActivityTypeViewModel: FirefighterActivityTypeViewModel = hiltViewModel(),
    inspectionTypeViewModel: InspectionTypeViewModel = hiltViewModel(),
    operationTypeViewModel: OperationTypeViewModel = hiltViewModel(),
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
    AppUiEvents(firefighterViewModel.uiEvents, snackbarHostState)
    AppUiEvents(assetTypeViewModel.uiEvents, snackbarHostState)
    AppUiEvents(firefighterActivityTypeViewModel.uiEvents, snackbarHostState)
    AppUiEvents(inspectionTypeViewModel.uiEvents, snackbarHostState)
    AppUiEvents(operationTypeViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.AdminScreen -> {
                    userViewModel.getUserByEmailAddress()
                    firefighterViewModel.getFirefighterByEmailAddress()
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
                        text = "${stringResource(id = R.string.hello)} ${currentUserUiState.currentUser?.firstName.orEmpty()} ${currentUserUiState.currentUser?.lastName.orEmpty()}!",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    DashboardGrid(
                        onOpenFiredepartments = { section = AdminSection.Firedepartments },
                        onOpenPresidents = { section = AdminSection.Presidents },
                        onOpenAssetTypes = { section = AdminSection.AssetTypes },
                        onOpenInspectionTypes = { section = AdminSection.InspectionTypes },
                        onOpenOperationTypes = { section = AdminSection.OperationTypes },
                        onOpenFirefighterActivityTypes = {
                            section = AdminSection.FirefighterActivityTypes
                        },
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

                AdminSection.Presidents -> {
                    PresidentsHub(
                        firefighterViewModel = firefighterViewModel,
                        firedepartmentViewModel = firedepartmentViewModel
                    )
                }

                AdminSection.AssetTypes -> {
                    AssetTypesHub(assetTypeViewModel)
                }

                AdminSection.FirefighterActivityTypes -> {
                    FirefighterActivityTypesHub(firefighterActivityTypeViewModel)
                }

                AdminSection.InspectionTypes -> {
                    InspectionTypesHub(inspectionTypeViewModel)
                }

                AdminSection.OperationTypes -> {
                    OperationTypesHub(operationTypeViewModel)
                }
            }
        }
    }
}

@Composable
private fun DashboardGrid(
    onOpenFiredepartments: () -> Unit,
    onOpenPresidents: () -> Unit,
    onOpenAssetTypes: () -> Unit,
    onOpenFirefighterActivityTypes: () -> Unit,
    onOpenInspectionTypes: () -> Unit,
    onOpenOperationTypes: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tiles = listOf(
        Triple(
            stringResource(id = R.string.firedepartments),
            Icons.Filled.Home,
            onOpenFiredepartments
        ),
        Triple(stringResource(id = R.string.presidents), Icons.Filled.Person, onOpenPresidents),
        Triple(stringResource(id = R.string.asset_types), Icons.Filled.Edit, onOpenAssetTypes),
        Triple(
            stringResource(id = R.string.firefighter_activity_types),
            Icons.Filled.Edit,
            onOpenFirefighterActivityTypes
        ),
        Triple(
            stringResource(id = R.string.inspection_types),
            Icons.Filled.Edit,
            onOpenInspectionTypes
        ),
        Triple(
            stringResource(id = R.string.operation_types),
            Icons.Filled.Edit,
            onOpenOperationTypes
        ),
        Triple(stringResource(id = R.string.logout), Icons.AutoMirrored.Filled.ArrowBack, onLogout),
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 180.dp),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
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