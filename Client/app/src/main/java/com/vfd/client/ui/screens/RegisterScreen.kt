package com.vfd.client.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.R
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppAddressActions
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppSectionHeader
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val registerUiState by authViewModel.registerUiState.collectAsState()

    AppUiEvents(authViewModel.uiEvents, snackbarHostState)

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
        AppSectionHeader(stringResource(id = R.string.personal_data))
        AppTextField(
            value = registerUiState.firstName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(firstName = new) } },
            label = stringResource(id = R.string.first_name),
            errorMessage = registerUiState.fieldErrors["firstName"]
        )
        AppTextField(
            value = registerUiState.lastName,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(lastName = new) } },
            label = stringResource(id = R.string.last_name),
            errorMessage = registerUiState.fieldErrors["lastName"]
        )

        AppSectionHeader(stringResource(id = R.string.address))
        AppAddressActions(
            address = registerUiState.address,
            errors = registerUiState.fieldErrors,
            onAddressChange = { newAddress ->
                authViewModel.onRegisterValueChange {
                    it.copy(address = newAddress, addressTouched = true)
                }
            },
            onTouched = {
                authViewModel.onRegisterValueChange {
                    it.copy(
                        addressTouched = true
                    )
                }
            }
        )

        AppSectionHeader(stringResource(id = R.string.contact))
        AppTextField(
            value = registerUiState.emailAddress,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(emailAddress = new) } },
            label = stringResource(id = R.string.email_address),
            errorMessage = registerUiState.fieldErrors["emailAddress"]
        )

        AppTextField(
            value = registerUiState.phoneNumber,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(phoneNumber = new) } },
            label = stringResource(id = R.string.phone_number),
            errorMessage = registerUiState.fieldErrors["phoneNumber"]
        )

        AppSectionHeader(stringResource(id = R.string.security))
        AppTextField(
            value = registerUiState.password,
            onValueChange = { new -> authViewModel.onRegisterValueChange { it.copy(password = new) } },
            label = stringResource(id = R.string.password),
            errorMessage = registerUiState.fieldErrors["password"],
            visualTransformation = PasswordVisualTransformation()
        )

        if (registerUiState.errorMessage != null) {
            AppErrorText(registerUiState.errorMessage!!)
        }

        AppButton(
            icon = Icons.Filled.Add,
            label = stringResource(id = R.string.register),
            onClick = { authViewModel.register() },
            fullWidth = true,
            enabled =
                registerUiState.firstName.isNotBlank() && registerUiState.lastName.isNotBlank() && registerUiState.address.country.isNotBlank()
                        && registerUiState.address.voivodeship.isNotBlank() && registerUiState.address.city.isNotBlank() && registerUiState.address.postalCode.isNotBlank()
                        && registerUiState.address.street.isNotBlank() && registerUiState.address.houseNumber.isNotBlank() && registerUiState.emailAddress.isNotBlank() && registerUiState.phoneNumber.isNotBlank()
                        && registerUiState.password.isNotBlank() && !registerUiState.isLoading,
            loading = registerUiState.isLoading
        )
    }
}