package com.vfd.client.ui.components.globals

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Right
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.vfd.client.ui.components.dialogs.AssetCreateDialog
import com.vfd.client.ui.components.dialogs.EventCreateDialog
import com.vfd.client.ui.components.dialogs.OperationCreateDialog
import com.vfd.client.ui.screens.AssetScreen
import com.vfd.client.ui.screens.EventScreen
import com.vfd.client.ui.screens.FirefighterScreen
import com.vfd.client.ui.screens.InfoScreen
import com.vfd.client.ui.screens.LoginScreen
import com.vfd.client.ui.screens.MeScreen
import com.vfd.client.ui.screens.ModeratorScreen
import com.vfd.client.ui.screens.NewFirefighterScreen
import com.vfd.client.ui.screens.OperationScreen
import com.vfd.client.ui.screens.RegisterScreen
import com.vfd.client.ui.screens.WelcomeScreen
import com.vfd.client.ui.viewmodels.AssetViewModel
import com.vfd.client.ui.viewmodels.EventViewModel
import com.vfd.client.ui.viewmodels.OperationViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState
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
            MeScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("infoScreen") {
            InfoScreen(navController = navController)
        }
        composable("moderatorScreen") {
            ModeratorScreen(navController = navController)
        }
        composable("newFirefighterScreen") {
            NewFirefighterScreen(
                navController = navController, snackbarHostState = snackbarHostState
            )
        }
        composable("firefighterScreen") {
            FirefighterScreen(navController = navController, snackbarHostState = snackbarHostState)
        }

        navigation(startDestination = "assets/list", route = "assets_graph") {

            composable("assets/list") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("assets_graph")
                }
                val assetViewModel: AssetViewModel = hiltViewModel(parentEntry)

                AssetScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    assetViewModel = assetViewModel
                )
            }

            dialog("assets/create") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("assets_graph")
                }
                val assetViewModel: AssetViewModel = hiltViewModel(parentEntry)

                AssetCreateDialog(
                    assetViewModel = assetViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        navigation(startDestination = "events/list", route = "events_graph") {

            composable("events/list") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("events_graph")
                }
                val eventViewModel: EventViewModel = hiltViewModel(parentEntry)

                EventScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    eventViewModel = eventViewModel
                )
            }

            dialog("events/create") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("events_graph")
                }
                val eventViewModel: EventViewModel = hiltViewModel(parentEntry)

                EventCreateDialog(
                    eventViewModel = eventViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        navigation(startDestination = "operations/list", route = "operations_graph") {

            composable("operations/list") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("operations_graph")
                }
                val operationViewModel: OperationViewModel = hiltViewModel(parentEntry)

                OperationScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    operationViewModel = operationViewModel
                )
            }

            dialog("operations/create") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("operations_graph")
                }
                val operationViewModel: OperationViewModel = hiltViewModel(parentEntry)

                OperationCreateDialog(
//                    operationViewModel = operationViewModel,
//                    showDialog = true,
//                    onDismiss = { navController.popBackStack() },
//                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}