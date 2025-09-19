package com.vfd.client

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vfd.client.ui.components.AppNavGraph
import com.vfd.client.ui.components.NavBarAction
import com.vfd.client.ui.components.NavBarButton
import com.vfd.client.ui.theme.MyVFDMobileTheme
import com.vfd.client.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                LaunchedEffect(currentRoute) {
                    if (currentRoute == "firefighterScreen") {
                        mainViewModel.refreshBadges()
                    }
                }
                Scaffold(
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
                                        "assetScreen" -> "Assets"
                                        else -> "My VFD"
                                    }
                                )
                            }
                        )
                    },
                    bottomBar = {
                        when (currentRoute) {
                            "meScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "TO DO",
                                        Icons.Default.Person,
                                        { /* navController.navigate("TO DO") */ }),
                                    NavBarButton(
                                        "TO DO",
                                        Icons.Default.Person,
                                        { /* navController.navigate("TO DO") */ }),
                                    NavBarButton(
                                        "TO DO",
                                        Icons.Default.Person,
                                        { /* navController.navigate("TO DO") */ }),
                                    NavBarButton(
                                        "TO DO",
                                        Icons.Default.Person,
                                        { /* navController.navigate("TO DO") */ })
                                )
                                NavBarAction(actions)
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
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                NavBarAction(actions)
                            }

                            "loginScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                NavBarAction(actions)
                            }

                            "registerScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                NavBarAction(actions)
                            }

                            "moderatorScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Firefighters",
                                        Icons.Default.Person,
                                        { navController.navigate("firefighterScreen") }
                                    ),
                                    NavBarButton(
                                        "Assets",
                                        Icons.Default.Build,
                                        { navController.navigate("assetScreen") }),
                                    NavBarButton(
                                        "Events",
                                        Icons.Default.Favorite,
                                        {  /* navController.navigate("eventScreen")*/ }),
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
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                NavBarAction(actions)
                            }

                            "assetScreen" -> {
                                val actions = listOf(
                                    NavBarButton(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                NavBarAction(actions)
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}