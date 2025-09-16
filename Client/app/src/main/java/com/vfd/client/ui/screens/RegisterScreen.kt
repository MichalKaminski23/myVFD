package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.vfd.client.ui.components.ActionButton
import com.vfd.client.ui.components.FormTextField
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SectionHeader("Personal data")
        FormTextField(
            value = registerUiState.firstName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(firstName = new) } },
            label = "First name",
            errorMessage = registerUiState.fieldErrors["firstName"]
        )
        FormTextField(
            value = registerUiState.lastName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(lastName = new) } },
            label = "Last name",
            errorMessage = registerUiState.fieldErrors["lastName"]
        )

        SectionHeader("Address")
        FormTextField(
            value = registerUiState.country,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(country = new) } },
            label = "Country",
            errorMessage = registerUiState.fieldErrors["address.country"]
        )

        FormTextField(
            value = registerUiState.voivodeship,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(voivodeship = new) } },
            label = "Voivodeship",
            errorMessage = registerUiState.fieldErrors["address.voivodeship"]
        )

        FormTextField(
            value = registerUiState.city,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(city = new) } },
            label = "City",
            errorMessage = registerUiState.fieldErrors["address.city"]
        )

        FormTextField(
            value = registerUiState.postalCode,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(postalCode = new) } },
            label = "Postal code",
            errorMessage = registerUiState.fieldErrors["address.postalCode"]
        )

        FormTextField(
            value = registerUiState.street,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(street = new) } },
            label = "Street",
            errorMessage = registerUiState.fieldErrors["address.street"]
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FormTextField(
                value = registerUiState.houseNumber,
                onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(houseNumber = new) } },
                label = "House number",
                errorMessage = registerUiState.fieldErrors["address.houseNumber"],
                modifier = Modifier.weight(1f)
            )
            FormTextField(
                value = registerUiState.apartNumber,
                onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(apartNumber = new) } },
                label = "Apartment number",
                errorMessage = registerUiState.fieldErrors["address.apartNumber"],
                modifier = Modifier.weight(1f)
            )
        }

        SectionHeader("Contact")
        FormTextField(
            value = registerUiState.emailAddress,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(emailAddress = new) } },
            label = "Email address",
            errorMessage = registerUiState.fieldErrors["emailAddress"]
        )

        FormTextField(
            value = registerUiState.phoneNumber,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(phoneNumber = new) } },
            label = "Phone number",
            errorMessage = registerUiState.fieldErrors["phoneNumber"]
        )

        SectionHeader("Security")
        FormTextField(
            value = registerUiState.password,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(password = new) } },
            label = "Password",
            errorMessage = registerUiState.fieldErrors["password"],
            visualTransformation = PasswordVisualTransformation()
        )

        if (registerUiState.error != null) {
            Text(
                text = registerUiState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 2.dp)
            )
        }

        ActionButton(
            icon = Icons.Filled.Lock,
            label = "Register",
            onClick = { authViewModel.register() },
            fullWidth = true,
            enabled = !registerUiState.loading,
            loading = registerUiState.loading
        )
    }
}