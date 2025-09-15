package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.FormTextField
import com.vfd.client.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val loginUiState by authViewModel.loginUiState.collectAsState()

    LaunchedEffect(loginUiState.success) {
        if (loginUiState.success) {
            navController.navigate("mainScreen") {
                popUpTo("loginScreen") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormTextField(
            value = loginUiState.emailAddress,
            onValueChange = { new -> authViewModel.onLoginValueChange { it.copy(emailAddress = new) } },
            label = "Email address",
            errorMessage = loginUiState.fieldErrors["emailAddress"],
            modifier = Modifier.fillMaxWidth()
        )
        FormTextField(
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
        Button(
            onClick = { authViewModel.login() },
            enabled = !loginUiState.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loginUiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }
    }
}