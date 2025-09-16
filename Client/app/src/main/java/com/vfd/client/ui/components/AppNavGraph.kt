package com.vfd.client.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vfd.client.ui.screens.InfoScreen
import com.vfd.client.ui.screens.LoginScreen
import com.vfd.client.ui.screens.MeScreen
import com.vfd.client.ui.screens.ModeratorScreen
import com.vfd.client.ui.screens.RegisterScreen
import com.vfd.client.ui.screens.WelcomeScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = "welcomeScreen",
        modifier = modifier,
        enterTransition = { slideIntoContainer(Left) + fadeIn(animationSpec = tween(500)) },
        exitTransition = { slideOutOfContainer(Left) + fadeOut(animationSpec = tween(500)) },
        popEnterTransition = { slideIntoContainer(Right) + fadeIn(animationSpec = tween(500)) },
        popExitTransition = { slideOutOfContainer(Right) + fadeOut(animationSpec = tween(500)) }
    ) {
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