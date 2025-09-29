package com.vfd.client.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.UserViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {
    val loginUiState by authViewModel.loginUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    LaunchedEffect(loginUiState.success) {
        if (loginUiState.success) {
            userViewModel.getUserByEmailAddress()
            firefighterViewModel.getFirefighterByEmailAddress()
        }
    }

    var routed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(loginUiState.success, currentFirefighterUiState) {
        if (!loginUiState.success || routed) return@LaunchedEffect
        if (currentFirefighterUiState.isLoading) return@LaunchedEffect

        when {
            currentFirefighterUiState.currentFirefighter?.role.toString() == FirefighterRole.PRESIDENT.toString() -> {
                routed = true
                navController.navigate("moderatorScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            currentFirefighterUiState.currentFirefighter != null -> {
                routed = true
                navController.navigate("meScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            currentFirefighterUiState.notFound || currentFirefighterUiState.success -> {
                routed = true
                navController.navigate("meScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            currentFirefighterUiState.errorMessage != null -> {
                routed = true
                navController.navigate("meScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        AppTextField(
            value = loginUiState.emailAddress,
            onValueChange = { new -> authViewModel.onLoginValueChange { it.copy(emailAddress = new) } },
            label = "Email address",
            errorMessage = loginUiState.fieldErrors["emailAddress"]
        )
        AppTextField(
            value = loginUiState.password,
            onValueChange = { new -> authViewModel.onLoginValueChange { it.copy(password = new) } },
            label = "Password",
            errorMessage = loginUiState.fieldErrors["password"],
            visualTransformation = PasswordVisualTransformation()
        )

        if (loginUiState.errorMessage != null) {
            AppErrorText(loginUiState.errorMessage!!)
        }

        AppButton(
            icon = Icons.Filled.Lock,
            label = "Login",
            onClick = { authViewModel.login() },
            fullWidth = true,
            enabled = loginUiState.emailAddress.isNotBlank() && loginUiState.password.isNotBlank() && !loginUiState.isLoading,
            loading = loginUiState.isLoading
        )
    }
}