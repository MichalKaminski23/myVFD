package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    // jeśli sukces → przenieś na ekran user
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            navController.navigate("user") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Authentication", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Create a new account or log in",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
            }

            // Dane osobowe
            item { SectionHeader("Personal data") }
            item {
                OutlinedTextField(
                    value = uiState.firstName,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(firstName = new) } },
                    label = { Text("First name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.lastName,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(lastName = new) } },
                    label = { Text("Last name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Adres
            item { SectionHeader("Address") }
            item {
                OutlinedTextField(
                    value = uiState.country,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(country = new) } },
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.voivodeship,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(voivodeship = new) } },
                    label = { Text("Voivodeship") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.city,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(city = new) } },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.postalCode,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(postalCode = new) } },
                    label = { Text("Postal code") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.street,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(street = new) } },
                    label = { Text("Street") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.houseNumber,
                        onValueChange = { new -> viewModel.onValueChange { it.copy(houseNumber = new) } },
                        label = { Text("House no.") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = uiState.apartNumber,
                        onValueChange = { new -> viewModel.onValueChange { it.copy(apartNumber = new) } },
                        label = { Text("Apartment no.") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Kontakt
            item { SectionHeader("Contact") }
            item {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(email = new) } },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(phone = new) } },
                    label = { Text("Phone number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Hasło
            item { SectionHeader("Security") }
            item {
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { new -> viewModel.onValueChange { it.copy(password = new) } },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            if (uiState.error != null) {
                item {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                Button(
                    onClick = { viewModel.register() },
                    enabled = !uiState.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Register")
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.login() },
                    enabled = !uiState.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.loading) {
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
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 4.dp)
    )
}
