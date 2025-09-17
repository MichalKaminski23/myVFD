package com.vfd.client

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vfd.client.ui.components.ActionsNavigationBar
import com.vfd.client.ui.components.AppNavGraph
import com.vfd.client.ui.components.NavActionSpec
import com.vfd.client.ui.theme.MyVFDMobileTheme
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
                                        "newFirefightersScreen" -> "New Firefighters"
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
                                    NavActionSpec(
                                        "Users",
                                        Icons.Default.Person,
                                        { navController.navigate("registerScreen") }),
                                    NavActionSpec(
                                        "Departments",
                                        Icons.Default.Home,
                                        { navController.navigate("meScreen") }),
                                    NavActionSpec(
                                        "Reports",
                                        Icons.Default.List,
                                        { navController.navigate("infoScreen") }),
                                    NavActionSpec("Settings", Icons.Default.Settings, { /* ... */ })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "welcomeScreen" -> {
                                val activity = (LocalContext.current as? Activity)
                                val actions = listOf(
                                    NavActionSpec(
                                        "Exit",
                                        Icons.Default.Close,
                                        { activity?.finish() })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "infoScreen" -> {
                                val actions = listOf(
                                    NavActionSpec(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "loginScreen" -> {
                                val actions = listOf(
                                    NavActionSpec(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "registerScreen" -> {
                                val actions = listOf(
                                    NavActionSpec(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "moderatorScreen" -> {
                                val actions = listOf(
                                    NavActionSpec(
                                        "New firefighters",
                                        Icons.Default.Person,
                                        { navController.navigate("newFirefightersScreen") }),
                                    NavActionSpec(
                                        "Departments",
                                        Icons.Default.Home,
                                        { navController.navigate("meScreen") }),
                                    NavActionSpec(
                                        "Reports",
                                        Icons.Default.List,
                                        { navController.navigate("infoScreen") }),
                                    NavActionSpec("Settings", Icons.Default.Settings, { /* ... */ })
                                )
                                ActionsNavigationBar(actions)
                            }

                            "newFirefightersScreen" -> {
                                val actions = listOf(
                                    NavActionSpec(
                                        "Back",
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        { navController.popBackStack() })
                                )
                                ActionsNavigationBar(actions)
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}