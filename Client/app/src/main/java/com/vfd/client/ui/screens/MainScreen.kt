package com.vfd.client.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.Role
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel()
) {
    val token by authViewModel.token.collectAsState()
    val currentFirefighter by firefighterViewModel.firefighter.collectAsState()

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            firefighterViewModel.getCurrentFirefighter()
        }
    }

    LaunchedEffect(token, currentFirefighter) {
        if (token.isNullOrBlank()) {
            navController.navigate("welcomeScreen") {
                popUpTo(0)
            }
        } else {
            when (currentFirefighter?.role) {
                Role.PRESIDENT -> {
                    navController.navigate("moderatorScreen") {
                        popUpTo("mainScreen") { inclusive = true }
                    }
                }

                Role.MEMBER, Role.USER -> {
                    navController.navigate("meScreen") {
                        popUpTo("mainScreen") { inclusive = true }
                    }
                }

                null -> {
                    
                }

                Role.ADMIN -> TODO()
            }
        }
    }
}