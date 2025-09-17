package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vfd.client.ui.components.ActionButton

@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Volunteer Fire Department",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(40.dp))
            ActionButton(
                icon = Icons.Filled.Lock,
                label = "Login",
                onClick = { navController.navigate("loginScreen") },
                fullWidth = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActionButton(
                icon = Icons.Filled.Lock,
                label = "Register",
                onClick = { navController.navigate("registerScreen") },
                fullWidth = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            ActionButton(
                icon = Icons.Filled.Info,
                label = "Information",
                onClick = { navController.navigate("infoScreen") },
                fullWidth = true
            )
        }
    }
}