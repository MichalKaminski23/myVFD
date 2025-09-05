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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.SectionHeader
import com.vfd.client.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {

    val registerUiState by authViewModel.registerUiState.collectAsState()

    LaunchedEffect(registerUiState.success) {
        if (registerUiState.success) {
            navController.navigate("meScreen") {
                popUpTo("registerScreen") { inclusive = true }
            }
        }
    }

    Scaffold { paddingValues ->
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
                    "Create a new account",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
            }

            item { SectionHeader("Personal data") }
            item {
                OutlinedTextField(
                    value = registerUiState.firstName,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(firstName = new) } },
                    label = { Text("First name") },
                    isError = registerUiState.fieldErrors.containsKey("firstName"),
                    modifier = Modifier.fillMaxWidth()
                )
                if (registerUiState.fieldErrors["firstName"] != null) {
                    Text(
                        text = registerUiState.fieldErrors["firstName"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = registerUiState.lastName,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(lastName = new) } },
                    label = { Text("Last name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { SectionHeader("Address") }
            item {
                OutlinedTextField(
                    value = registerUiState.country,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(country = new) } },
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = registerUiState.voivodeship,
                    onValueChange = { new ->
                        authViewModel.onRegisterValueChange {
                            it.copy(
                                voivodeship = new
                            )
                        }
                    },
                    label = { Text("Voivodeship") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = registerUiState.city,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(city = new) } },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = registerUiState.postalCode,
                    onValueChange = { new ->
                        authViewModel.onRegisterValueChange {
                            it.copy(
                                postalCode = new
                            )
                        }
                    },
                    label = { Text("Postal code") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = registerUiState.street,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(street = new) } },
                    label = { Text("Street") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = registerUiState.houseNumber,
                        onValueChange = { new ->
                            authViewModel.onRegisterValueChange {
                                it.copy(
                                    houseNumber = new
                                )
                            }
                        },
                        label = { Text("House no.") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = registerUiState.apartNumber,
                        onValueChange = { new ->
                            authViewModel.onRegisterValueChange {
                                it.copy(
                                    apartNumber = new
                                )
                            }
                        },
                        label = { Text("Apartment no.") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionHeader("Contact") }
            item {
                OutlinedTextField(
                    value = registerUiState.emailAddress,
                    onValueChange = { new ->
                        authViewModel.onRegisterValueChange {
                            it.copy(
                                emailAddress = new
                            )
                        }
                    },
                    label = { Text("Email") },
                    isError = registerUiState.fieldErrors.containsKey("emailAddress"),
                    modifier = Modifier.fillMaxWidth()
                )
                if (registerUiState.fieldErrors["emailAddress"] != null) {
                    Text(
                        text = registerUiState.fieldErrors["emailAddress"]!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                OutlinedTextField(
                    value = registerUiState.phoneNumber,
                    onValueChange = { new ->
                        authViewModel.onRegisterValueChange {
                            it.copy(
                                phoneNumber = new
                            )
                        }
                    },
                    label = { Text("Phone number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { SectionHeader("Security") }
            item {
                OutlinedTextField(
                    value = registerUiState.password,
                    onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(password = new) } },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            if (registerUiState.error != null) {
                item {
                    Text(
                        text = registerUiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                Button(
                    onClick = { authViewModel.register() },
                    enabled = !registerUiState.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (registerUiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Register")
                    }
                }
            }
        }
    }
}