package com.vfd.client

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vfd.client.ui.components.buttons.NavBarAction
import com.vfd.client.ui.components.buttons.NavBarButton
import com.vfd.client.ui.components.globals.AppNavGraph
import com.vfd.client.ui.theme.MyVFDMobileTheme
import com.vfd.client.ui.viewmodels.MainViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVFDMobileTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val mainViewModel: MainViewModel = hiltViewModel()
                val pending by mainViewModel.pendingFirefighters.collectAsState()
                val active by mainViewModel.activeFirefighters.collectAsState()
                val assets by mainViewModel.totalAssets.collectAsState()
                val canCreateThings by mainViewModel.canCreateThings.collectAsState()
                val hasRole by mainViewModel.hasRole.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                val snackbarShape = RoundedCornerShape(12.dp)
                LaunchedEffect(currentRoute) {
                    when (currentRoute) {
                        "moderatorScreen" -> mainViewModel.refreshBadges()
                        "firefighterScreen" -> mainViewModel.refreshBadges()
                    }
                }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = { snackbarData ->
                                Snackbar(
                                    snackbarData = snackbarData,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    actionColor = MaterialTheme.colorScheme.onPrimary,
                                    actionContentColor = MaterialTheme.colorScheme.primary,
                                    shape = snackbarShape,
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            shape = snackbarShape
                                        )
                                        .clip(snackbarShape)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = snackbarShape
                                        )
                                )
                            }
                        )
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    when (currentRoute) {
                                        "meScreen" -> "My Profile"
                                        "registerScreen" -> "Register"
                                        "loginScreen" -> "Login"
                                        "infoScreen" -> "Information"
                                        "moderatorScreen" -> "Moderate"
                                        "newFirefighterScreen" -> "New Firefighters"
                                        "welcomeScreen" -> "My VFD"
                                        "firefighterScreen" -> "Firefighters"
                                        "assets/list" -> "Assets"
                                        "events/list" -> "Events"
                                        else -> "My VFD"
                                    }
                                )
                            },
                            actions = {
                                when (currentRoute) {
                                    "meScreen" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    "moderatorScreen" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    "newFirefighterScreen" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    "firefighterScreen" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    "assets/list" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    "events/list" -> {
                                        RefreshButton(currentRoute)
                                    }

                                    else -> {
                                        // No action
                                    }
                                }
                            }
                        )

                    },
                    bottomBar = {
                        when (currentRoute) {
                            "meScreen" -> {
                                if (hasRole) {
                                    val actions = listOf(
                                        NavBarButton(
                                            "TO DO",
                                            Icons.Default.Person,
                                            { /* navController.navigate("TO DO") */ }),
                                        NavBarButton(
                                            "Assets",
                                            Icons.Default.Build,
                                            {
                                                navController.navigate("assets/list") {
                                                    launchSingleTop = true
                                                }
                                            },
                                        ),
                                        NavBarButton(
                                            "Events",
                                            Icons.Default.Favorite,
                                            {
                                                navController.navigate("events/list") {
                                                    launchSingleTop = true
                                                }
                                            }),
                                        NavBarButton(
                                            "TO DO",
                                            Icons.Default.Person,
                                            { /* navController.navigate("TO DO") */ })
                                    )
                                    NavBarAction(actions)
                                }

                            }

                            "welcomeScreen" -> {
                                val activity = (LocalContext.current as? Activity)
                                val actions = listOf(
                                    NavBarButton(
                                        "Exit",
                                        Icons.Default.Close,
                                        { activity?.finish() })
                                )
                                NavBarAction(actions)
                            }

                            "infoScreen" -> {
                                GoBackButton(navController)
                            }

                            "loginScreen" -> {
                                GoBackButton(navController)
                            }

                            "registerScreen" -> {
                                GoBackButton(navController)
                            }

                            "moderatorScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Firefighters",
                                        Icons.Default.Person,
                                        { navController.navigate("firefighterScreen") },
                                        badgeCount = active
                                    ),
                                    NavBarButton(
                                        "Assets",
                                        Icons.Default.Build,
                                        {
                                            navController.navigate("assets/list") {
                                                launchSingleTop = true
                                            }
                                        },
                                        badgeCount = assets
                                    ),
                                    NavBarButton(
                                        "Events",
                                        Icons.Default.Favorite,
                                        {
                                            navController.navigate("events/list") {
                                                launchSingleTop = true
                                            }
                                        }),
                                    NavBarButton(
                                        "Operations",
                                        Icons.Default.Settings,
                                        { /* navController.navigate("operationScreen") */ }
                                    ),
                                    NavBarButton(
                                        "Investments",
                                        Icons.Default.ShoppingCart,
                                        { /* navController.navigate("investmentScreen") */ }
                                    )
                                )
                                NavBarAction(actions)
                            }

                            "firefighterScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() }),
                                    NavBarButton(
                                        "Pending Firefighters",
                                        Icons.Default.Person,
                                        { navController.navigate("newFirefighterScreen") },
                                        badgeCount = pending
                                    )
                                )
                                NavBarAction(actions)
                            }

                            "newFirefighterScreen" -> {
                                GoBackButton(navController)
                            }

                            "assets/list" -> {
                                val actions = mutableListOf<NavBarButton>()
                                actions.add(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() }),
                                )
                                if (canCreateThings) {
                                    actions.add(
                                        NavBarButton(
                                            "Create asset",
                                            Icons.Default.DateRange,
                                            {
                                                navController.navigate("assets/create") {
                                                    launchSingleTop = true
                                                }
                                            }
                                        )
                                    )
                                }
                                NavBarAction(actions)
                            }

                            "events/list" -> {
                                val actions = mutableListOf<NavBarButton>()
                                actions.add(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() }),
                                )
                                if (canCreateThings) {
                                    actions.add(
                                        NavBarButton(
                                            "Create event",
                                            Icons.Default.DateRange,
                                            {
                                                navController.navigate("events/create") {
                                                    launchSingleTop = true
                                                }
                                            }
                                        )
                                    )
                                }
                                NavBarAction(actions)
                            }

                            else -> {
                                // No action
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
private fun GoBackButton(navController: NavHostController) {
    val actions = listOf(
        NavBarButton(
            "Back",
            Icons.AutoMirrored.Filled.ArrowBack,
            { navController.popBackStack() })
    )
    NavBarAction(actions)
}

@Composable
fun RefreshButton(currentRoute: String?) {
    val scope = rememberCoroutineScope()

    val refreshEvent = when (currentRoute) {
        "meScreen" -> RefreshEvent.MeScreen
        "moderatorScreen" -> RefreshEvent.ModeratorScreen
        "newFirefighterScreen" -> RefreshEvent.NewFirefighterScreen
        "firefighterScreen" -> RefreshEvent.FirefighterScreen
        "assets/list" -> RefreshEvent.AssetScreen
        "events/list" -> RefreshEvent.EventScreen
        else -> null
    }

    if (refreshEvent != null) {
        IconButton(
            onClick = { scope.launch { RefreshManager.refresh(refreshEvent) } }
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh"
            )
        }
    }
}