package com.vfd.client.ui.components.globals

import android.net.Uri
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.vfd.client.ui.components.dialogs.AssetCreateDialog
import com.vfd.client.ui.components.dialogs.EventCreateDialog
import com.vfd.client.ui.components.dialogs.FirefighterActivityCreateDialog
import com.vfd.client.ui.components.dialogs.InspectionCreateDialog
import com.vfd.client.ui.components.dialogs.InvestmentProposalCreateDialog
import com.vfd.client.ui.components.dialogs.OperationCreateDialog
import com.vfd.client.ui.screens.AdminScreen
import com.vfd.client.ui.screens.AssetScreen
import com.vfd.client.ui.screens.EventScreen
import com.vfd.client.ui.screens.FirefighterActivityScreen
import com.vfd.client.ui.screens.FirefighterScreen
import com.vfd.client.ui.screens.InfoScreen
import com.vfd.client.ui.screens.InspectionScreen
import com.vfd.client.ui.screens.InvestmentProposalScreen
import com.vfd.client.ui.screens.LoginScreen
import com.vfd.client.ui.screens.MeScreen
import com.vfd.client.ui.screens.ModeratorScreen
import com.vfd.client.ui.screens.NewFirefighterScreen
import com.vfd.client.ui.screens.OperationScreen
import com.vfd.client.ui.screens.RegisterScreen
import com.vfd.client.ui.screens.WelcomeScreen
import com.vfd.client.ui.viewmodels.AssetViewModel
import com.vfd.client.ui.viewmodels.EventViewModel
import com.vfd.client.ui.viewmodels.FirefighterActivityViewModel
import com.vfd.client.ui.viewmodels.InspectionViewModel
import com.vfd.client.ui.viewmodels.InvestmentProposalViewModel
import com.vfd.client.ui.viewmodels.OperationViewModel


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
            LoginScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("registerScreen") {
            RegisterScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("meScreen") {
            MeScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("infoScreen") {
            InfoScreen(navController = navController)
        }
        composable("moderatorScreen") {
            ModeratorScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("newFirefighterScreen") {
            NewFirefighterScreen(
                navController = navController, snackbarHostState = snackbarHostState
            )
        }
        composable("firefighterScreen") {
            FirefighterScreen(navController = navController, snackbarHostState = snackbarHostState)
        }
        composable("adminScreen") {
            AdminScreen(navController = navController, snackbarHostState = snackbarHostState)
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

        navigation(
            startDestination = "inspections/list",
            route = "inspections_graph"
        ) {

            composable(
                route = "inspections/list?assetId={assetId}&assetName={assetName}",
                arguments = listOf(
                    navArgument("assetId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument("assetName") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("inspections_graph")
                }
                val inspectionViewModel: InspectionViewModel = hiltViewModel(parentEntry)

                val assetIdArg = backStackEntry.arguments?.getInt("assetId")
                val assetId = assetIdArg?.takeIf { it != -1 }

                val assetNameArg = backStackEntry.arguments?.getString("assetName")
                val assetName = assetNameArg?.let { Uri.decode(it) }

                InspectionScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    inspectionViewModel = inspectionViewModel,
                    assetId = assetId,
                    assetName = assetName
                )
            }

            dialog(
                route = "inspections/create?assetId={assetId}",
                arguments = listOf(
                    navArgument("assetId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->

                val assetIdArg = backStackEntry.arguments?.getInt("assetId")
                val assetId = assetIdArg?.takeIf { it != -1 }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("inspections_graph")
                }
                val inspectionViewModel: InspectionViewModel = hiltViewModel(parentEntry)

                InspectionCreateDialog(
                    inspectionViewModel = inspectionViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState,
                    assetId = assetId
                )
            }
        }

        navigation(
            startDestination = "activities/list",
            route = "activities_graph"
        ) {

            composable(
                route = "activities/list?firefighterId={firefighterId}&firstName={firstName}&lastName={lastName}&from={from}",
                arguments = listOf(
                    navArgument("firefighterId") {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                    navArgument("firstName") {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument("lastName") {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument("from") {
                        type = NavType.StringType
                        defaultValue = ""
                        nullable = true
                    }
                )
            ) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("activities_graph")
                }
                val firefighterActivityViewModel: FirefighterActivityViewModel =
                    hiltViewModel(parentEntry)

                val firefighterIdArg = backStackEntry.arguments?.getInt("firefighterId")
                val firefighterId = firefighterIdArg?.takeIf { it != -1 }

                val firefighterNameArg = backStackEntry.arguments?.getString("firstName")
                val firefighterName = firefighterNameArg?.let { Uri.decode(it) }

                val lastNameArg = backStackEntry.arguments?.getString("lastName")
                val lastName = lastNameArg?.let { Uri.decode(it) }

                val from =
                    backStackEntry.arguments?.getString("from")?.takeIf { it?.isNotBlank() == true }

                FirefighterActivityScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    firefighterActivityViewModel = firefighterActivityViewModel,
                    firefighterId = firefighterId,
                    firefighterName = firefighterName,
                    firefighterLastName = lastName
                )
            }

            dialog(
                route = "activities/create?firefighterId={firefighterId}",
                arguments = listOf(
                    navArgument("firefighterId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) { backStackEntry ->

                val firefighterIdArg = backStackEntry.arguments?.getInt("firefighterId")
                val firefighterId = firefighterIdArg?.takeIf { it != -1 }

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("activities_graph")
                }
                val firefighterActivityViewModel: FirefighterActivityViewModel =
                    hiltViewModel(parentEntry)

                FirefighterActivityCreateDialog(
                    firefighterActivityViewModel = firefighterActivityViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState,
                    firefighterId = firefighterId
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
                    operationViewModel = operationViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        navigation(
            startDestination = "investments/list",
            route = "investments_graph"
        ) {

            composable("investments/list") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("investments_graph")
                }
                val investmentProposalViewModel: InvestmentProposalViewModel =
                    hiltViewModel(parentEntry)

                InvestmentProposalScreen(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    investmentProposalViewModel = investmentProposalViewModel
                )
            }

            dialog("investments/create") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("investments_graph")
                }
                val investmentProposalViewModel: InvestmentProposalViewModel =
                    hiltViewModel(parentEntry)

                InvestmentProposalCreateDialog(
                    investmentProposalViewModel = investmentProposalViewModel,
                    showDialog = true,
                    onDismiss = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}