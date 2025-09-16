package com.vfd.client

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vfd.client.ui.components.ActionButton
import com.vfd.client.ui.components.AppNavGraph
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
                                        else -> "My VFD"
                                    }
                                )
                            }
                        )
                    },
                    bottomBar = {
                        when (currentRoute) {
                            "meScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        ActionButton(
                                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                                            label = "Back",
                                            onClick = { navController.popBackStack() },
                                        )
                                    }
                                }
                            }

                            "welcomeScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        val activity = (LocalContext.current as? Activity)
                                        ActionButton(
                                            icon = Icons.Default.Close,
                                            label = "Exit",
                                            onClick = { activity?.finish() },
                                        )
                                    }
                                }
                            }

                            "infoScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    )
                                    {
                                        ActionButton(
                                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                                            label = "Back",
                                            onClick = { navController.popBackStack() },
                                        )
                                    }
                                }
                            }

                            "loginScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    )
                                    {
                                        ActionButton(
                                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                                            label = "Back",
                                            onClick = { navController.popBackStack() },
                                        )
                                    }
                                }
                            }

                            "registerScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    )
                                    {
                                        ActionButton(
                                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                                            label = "Back",
                                            onClick = { navController.popBackStack() },
                                        )
                                    }
                                }
                            }

                            "moderatorScreen" -> {
                                NavigationBar {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    )
                                    {
                                        ActionButton(
                                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                                            label = "Back",
                                            onClick = { navController.popBackStack() },
                                        )
                                    }
                                }
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