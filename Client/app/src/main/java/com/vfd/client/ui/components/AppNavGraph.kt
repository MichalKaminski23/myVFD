package com.vfd.client.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vfd.client.ui.screens.InfoScreen
import com.vfd.client.ui.screens.LoginScreen
import com.vfd.client.ui.screens.MainScreen
import com.vfd.client.ui.screens.MeScreen
import com.vfd.client.ui.screens.ModeratorScreen
import com.vfd.client.ui.screens.RegisterScreen
import com.vfd.client.ui.screens.WelcomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen",
        modifier = modifier
    ) {
        composable("mainScreen") {
            MainScreen(navController = navController)
        }
        composable("welcomeScreen") {
            WelcomeScreen(navController = navController)
        }
        composable("loginScreen") {
            LoginScreen(navController = navController)
        }
        composable("registerScreen") {
            RegisterScreen(navController = navController)
        }
        composable("meScreen") {
            MeScreen(navController = navController)
        }
        composable("infoScreen") {
            InfoScreen(navController = navController)
        }
        composable("moderatorScreen") {
            ModeratorScreen(navController = navController)
        }
    }
}