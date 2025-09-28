package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppSectionHeader
import com.vfd.client.ui.components.AppTextField
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

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        AppSectionHeader("Personal data")
        AppTextField(
            value = registerUiState.firstName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(firstName = new) } },
            label = "First name",
            errorMessage = registerUiState.fieldErrors["firstName"]
        )
        AppTextField(
            value = registerUiState.lastName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(lastName = new) } },
            label = "Last name",
            errorMessage = registerUiState.fieldErrors["lastName"]
        )

        AppSectionHeader("Address")
        AppTextField(
            value = registerUiState.country,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(country = new) } },
            label = "Country",
            errorMessage = registerUiState.fieldErrors["address.country"]
        )

        AppTextField(
            value = registerUiState.voivodeship,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(voivodeship = new) } },
            label = "Voivodeship",
            errorMessage = registerUiState.fieldErrors["address.voivodeship"]
        )

        AppTextField(
            value = registerUiState.city,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(city = new) } },
            label = "City",
            errorMessage = registerUiState.fieldErrors["address.city"]
        )

        AppTextField(
            value = registerUiState.postalCode,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(postalCode = new) } },
            label = "Postal code",
            errorMessage = registerUiState.fieldErrors["address.postalCode"]
        )

        AppTextField(
            value = registerUiState.street,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(street = new) } },
            label = "Street",
            errorMessage = registerUiState.fieldErrors["address.street"]
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppTextField(
                value = registerUiState.houseNumber,
                onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(houseNumber = new) } },
                label = "House number",
                errorMessage = registerUiState.fieldErrors["address.houseNumber"],
                modifier = Modifier.weight(1.0f)
            )
            AppTextField(
                value = registerUiState.apartNumber,
                onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(apartNumber = new) } },
                label = "Apartment number",
                errorMessage = registerUiState.fieldErrors["address.apartNumber"],
                modifier = Modifier.weight(1.0f)
            )
        }

        AppSectionHeader("Contact")
        AppTextField(
            value = registerUiState.emailAddress,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(emailAddress = new) } },
            label = "Email address",
            errorMessage = registerUiState.fieldErrors["emailAddress"]
        )

        AppTextField(
            value = registerUiState.phoneNumber,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(phoneNumber = new) } },
            label = "Phone number",
            errorMessage = registerUiState.fieldErrors["phoneNumber"]
        )

        AppSectionHeader("Security")
        AppTextField(
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

        AppButton(
            icon = Icons.Filled.Add,
            label = "Register",
            onClick = { authViewModel.register() },
            fullWidth = true,
            enabled =
                registerUiState.firstName.isNotBlank() && registerUiState.lastName.isNotBlank() && registerUiState.country.isNotBlank()
                        && registerUiState.voivodeship.isNotBlank() && registerUiState.city.isNotBlank() && registerUiState.postalCode.isNotBlank()
                        && registerUiState.street.isNotBlank() && registerUiState.houseNumber.isNotBlank() && registerUiState.emailAddress.isNotBlank() && registerUiState.phoneNumber.isNotBlank()
                        && registerUiState.password.isNotBlank() && !registerUiState.loading,
            loading = registerUiState.loading
        )
    }
}