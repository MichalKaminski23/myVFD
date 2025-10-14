package com.vfd.client.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterCard
import com.vfd.client.ui.components.cards.AppUserCard
import com.vfd.client.ui.components.elements.AppAddressActions
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.MainViewModel
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    val currentUserUiState by userViewModel.currentUserUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    val userUpdateUiState by userViewModel.userUpdateUiState.collectAsState()

    AppUiEvents(userViewModel.uiEvents, snackbarHostState)

    var showHourInputs by remember { mutableStateOf(false) }
    var showUpdateInputs by remember { mutableStateOf(false) }
    var quarterInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }

    val mainViewModel: MainViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(currentFirefighterUiState.currentFirefighter?.role) {
        mainViewModel.setUserRole(currentFirefighterUiState.currentFirefighter?.role)
    }

    LaunchedEffect(userUpdateUiState.success) {
        if (userUpdateUiState.success) {
            showUpdateInputs = false
            userViewModel.getUserByEmailAddress()
        }
    }

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.ModeratorScreen -> {
                    userViewModel.getUserByEmailAddress()
                    firefighterViewModel.getFirefighterByEmailAddress()
                }

                else -> {}
            }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (currentUserUiState.isLoading) {
            AppText(
                "The data is loading...",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        currentUserUiState.currentUser?.let { user ->
            AppUserCard(user, actions = {
                if (showUpdateInputs) {
                    AppTextField(
                        value = userUpdateUiState.firstName,
                        onValueChange = { new ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    firstName = new,
                                    firstNameTouched = true
                                )
                            }
                        },
                        label = "First name",
                        errorMessage = userUpdateUiState.fieldErrors["firstName"]
                    )
                    AppTextField(
                        value = userUpdateUiState.lastName,
                        onValueChange = { new ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    lastName = new,
                                    lastNameTouched = true
                                )
                            }
                        },
                        label = "Last name",
                        errorMessage = userUpdateUiState.fieldErrors["lastName"]
                    )
                    AppTextField(
                        value = userUpdateUiState.emailAddress,
                        onValueChange = { new ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    emailAddress = new,
                                    emailAddressTouched = true
                                )
                            }
                        },
                        label = "Email address",
                        errorMessage = userUpdateUiState.fieldErrors["emailAddress"]
                    )
                    AppTextField(
                        value = userUpdateUiState.password,
                        onValueChange = { new ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    password = new,
                                    passwordTouched = true
                                )
                            }
                        },
                        label = "Password",
                        errorMessage = userUpdateUiState.fieldErrors["password"],
                        visualTransformation = PasswordVisualTransformation()
                    )

                    AppTextField(
                        value = userUpdateUiState.phoneNumber,
                        onValueChange = { new ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    phoneNumber = new,
                                    phoneNumberTouched = true
                                )
                            }
                        },
                        label = "Phone number",
                        errorMessage = userUpdateUiState.fieldErrors["phoneNumber"],
                    )

                    AppAddressActions(
                        address = userUpdateUiState.address,
                        errors = userUpdateUiState.fieldErrors,
                        onAddressChange = { newAddress ->
                            userViewModel.onUserUpdateValueChange {
                                it.copy(address = newAddress)
                            }
                        },
                        onTouched = {
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    addressTouched = true
                                )
                            }
                        }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Default.Check,
                            label = "Save",
                            onClick = {
                                user.userId.let { id ->
                                    val userDto = UserDtos.UserPatch(
                                        firstName = if (userUpdateUiState.firstNameTouched)
                                            userUpdateUiState.firstName
                                        else null,

                                        lastName = if (userUpdateUiState.lastNameTouched)
                                            userUpdateUiState.lastName
                                        else null,

                                        emailAddress = if (userUpdateUiState.emailAddressTouched)
                                            userUpdateUiState.emailAddress
                                        else null,

                                        phoneNumber = if (userUpdateUiState.phoneNumberTouched)
                                            userUpdateUiState.phoneNumber
                                        else null,

                                        password = if (userUpdateUiState.passwordTouched) {
                                            userUpdateUiState.password
                                        } else null,

                                        address = if (userUpdateUiState.addressTouched)
                                            userUpdateUiState.address
                                        else null,
                                    )
                                    userViewModel.updateUser(userDto)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = userUpdateUiState.firstName.isNotBlank() &&
                                    userUpdateUiState.lastName.isNotBlank() &&
                                    userUpdateUiState.emailAddress.isNotBlank() &&
                                    userUpdateUiState.phoneNumber.isNotBlank() &&
                                    userUpdateUiState.address?.country?.isNotBlank() == true &&
                                    userUpdateUiState.address?.voivodeship?.isNotBlank() == true &&
                                    userUpdateUiState.address?.city?.isNotBlank() == true &&
                                    userUpdateUiState.address?.postalCode?.isNotBlank() == true &&
                                    userUpdateUiState.address?.street?.isNotBlank() == true &&
                                    userUpdateUiState.address?.houseNumber?.isNotBlank() == true &&
                                    !userUpdateUiState.isLoading,
                            loading = userUpdateUiState.isLoading
                        )
                        AppButton(
                            icon = Icons.Default.Close,
                            label = "Cancel",
                            onClick = { showUpdateInputs = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (!showUpdateInputs) {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Edit",
                        onClick = {
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    firstName = currentUserUiState.currentUser?.firstName.orEmpty(),
                                    lastName = currentUserUiState.currentUser?.lastName.orEmpty(),
                                    address = AddressDtos.AddressCreate(
                                        country = currentUserUiState.currentUser?.address?.country.orEmpty(),
                                        voivodeship = currentUserUiState.currentUser?.address?.voivodeship.orEmpty(),
                                        city = currentUserUiState.currentUser?.address?.city.orEmpty(),
                                        postalCode = currentUserUiState.currentUser?.address?.postalCode.orEmpty(),
                                        street = currentUserUiState.currentUser?.address?.street.orEmpty(),
                                        houseNumber = currentUserUiState.currentUser?.address?.houseNumber.orEmpty(),
                                        apartNumber = currentUserUiState.currentUser?.address?.apartNumber.orEmpty()
                                    ),
                                    emailAddress = currentUserUiState.currentUser?.emailAddress.orEmpty(),
                                    phoneNumber = currentUserUiState.currentUser?.phoneNumber.orEmpty(),
                                    password = "",
                                    firstNameTouched = false,
                                    lastNameTouched = false,
                                    emailAddressTouched = false,
                                    phoneNumberTouched = false,
                                    passwordTouched = false,
                                    addressTouched = false,
                                    fieldErrors = emptyMap(),
                                    errorMessage = null,
                                    success = false,
                                    isLoading = false
                                )
                            }
                            showUpdateInputs = true
                        }
                    )
                }

            })
        }

        currentFirefighterUiState.currentFirefighter?.let { firefighter ->
            AppFirefighterCard(
                firefighter,
                quarterHours = currentFirefighterUiState.hours?.hours,
                actions = {
                    if (showHourInputs) {
                        AppTextField(
                            value = quarterInput,
                            onValueChange = { raw ->
                                val digits = raw.filter(Char::isDigit).take(1)
                                if (digits.isEmpty() || digits in listOf("1", "2", "3", "4")) {
                                    quarterInput = digits
                                }
                            },
                            label = "Quarter (1-4)",
                            errorMessage = currentFirefighterUiState.errorMessage,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        AppTextField(
                            value = yearInput,
                            onValueChange = { raw ->
                                yearInput = raw.filter(Char::isDigit).take(4)
                            },
                            label = "Year (YYYY)",
                            errorMessage = currentFirefighterUiState.errorMessage,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        AppButton(
                            icon = Icons.Default.ThumbUp,
                            label = "Show hours from quarter",
                            onClick = {
                                val quarter = quarterInput.toIntOrNull() ?: 0
                                val year = yearInput.toIntOrNull() ?: 0
                                firefighterViewModel.getHoursForQuarter(year, quarter)
                                showHourInputs = false
                            },
                            enabled = quarterInput.isNotEmpty() && yearInput.isNotEmpty()
                        )
                    }
                    if (!showHourInputs) {
                        AppButton(
                            icon = Icons.Default.ThumbUp,
                            label = "Show hours from quarter",
                            onClick = { showHourInputs = true }
                        )
                    }
                }
            )
        }

        AppLoadingBar(currentUserUiState.isLoading)
        AppLoadingBar(currentFirefighterUiState.isLoading)

        AppButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            label = "Logout",
            onClick = {
                authViewModel.logout()
                navController.navigate("welcomeScreen") {
                    popUpTo("moderatorScreen") { inclusive = true }
                }
            },
            fullWidth = true,
        )
    }
}