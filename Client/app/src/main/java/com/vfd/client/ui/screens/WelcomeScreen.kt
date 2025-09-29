package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText

@Composable
fun WelcomeScreen(navController: NavController) {
    AppColumn(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    )
    {
        Spacer(modifier = Modifier.height(100.dp))
        AppText("My Volunteer Fire Department", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(40.dp))
        AppText("Login or create a new account.", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(40.dp))
        AppButton(
            icon = Icons.Filled.Lock,
            label = "Login",
            onClick = { navController.navigate("loginScreen") },
            fullWidth = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            icon = Icons.Filled.Add,
            label = "Register",
            onClick = { navController.navigate("registerScreen") },
            fullWidth = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            icon = Icons.Filled.Info,
            label = "Information",
            onClick = { navController.navigate("infoScreen") },
            fullWidth = true
        )
    }
}