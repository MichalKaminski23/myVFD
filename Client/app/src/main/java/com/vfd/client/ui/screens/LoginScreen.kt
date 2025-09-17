package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.Role
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppTextField
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
    val currentFirefighter by firefighterViewModel.firefighter.collectAsState()
    val firefighterUiState by firefighterViewModel.firefighterUiState.collectAsState()

    LaunchedEffect(loginUiState.success) {
        if (loginUiState.success) {
            userViewModel.getCurrentUser()
            firefighterViewModel.getCurrentFirefighter()
        }
    }

    var routed by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(loginUiState.success, firefighterUiState, currentFirefighter) {
        if (!loginUiState.success || routed) return@LaunchedEffect
        if (firefighterUiState.loading) return@LaunchedEffect

        when {
            currentFirefighter?.role == Role.PRESIDENT -> {
                routed = true
                navController.navigate("moderatorScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            currentFirefighter != null -> {
                routed = true
                navController.navigate("meScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            firefighterUiState.notFound || firefighterUiState.success -> {
                routed = true
                navController.navigate("meScreen") {
                    popUpTo("welcomeScreen") { inclusive = false }
                    launchSingleTop = true
                }
            }

            firefighterUiState.error != null -> {
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
            errorMessage = loginUiState.fieldErrors["emailAddress"],
            modifier = Modifier.fillMaxWidth(),
        )
        AppTextField(
            value = loginUiState.password,
            onValueChange = { new -> authViewModel.onLoginValueChange { it.copy(password = new) } },
            label = "Password",
            errorMessage = loginUiState.fieldErrors["password"],
            visualTransformation = PasswordVisualTransformation()
        )

        if (loginUiState.error != null) {
            Text(
                text = loginUiState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 2.dp)
            )
        }

        AppButton(
            icon = Icons.Filled.Lock,
            label = "Login",
            onClick = { authViewModel.login() },
            fullWidth = true,
            enabled = !loginUiState.loading,
            loading = loginUiState.loading
        )
    }
}