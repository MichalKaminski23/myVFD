package com.vfd.client.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterCard
import com.vfd.client.ui.components.cards.AppUserCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppPasswordChangeForm
import com.vfd.client.ui.components.elements.AppSearchHoursForm
import com.vfd.client.ui.components.elements.AppUserEditForm
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
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
    var showPasswordInputs by remember { mutableStateOf(false) }

    val mainViewModel: MainViewModel = hiltViewModel(LocalContext.current as ComponentActivity)

    LaunchedEffect(currentFirefighterUiState.currentFirefighter?.role) {
        mainViewModel.setUserRole(currentFirefighterUiState.currentFirefighter?.role)
    }

    LaunchedEffect(userUpdateUiState.success) {
        if (userUpdateUiState.success) {
            showUpdateInputs = false
            showPasswordInputs = false
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

        currentUserUiState.errorMessage?.let { AppErrorText(it) }

        currentUserUiState.currentUser?.let { user ->
            AppUserCard(user, actions = {
                AppUserEditForm(
                    state = userUpdateUiState,
                    visible = showUpdateInputs,
                    onFirstNameChange = { new ->
                        userViewModel.onUserUpdateValueChange {
                            it.copy(firstName = new, firstNameTouched = true)
                        }
                    },
                    onLastNameChange = { new ->
                        userViewModel.onUserUpdateValueChange {
                            it.copy(lastName = new, lastNameTouched = true)
                        }
                    },
                    onEmailAddressChange = { new ->
                        userViewModel.onUserUpdateValueChange {
                            it.copy(emailAddress = new, emailAddressTouched = true)
                        }
                    },
                    onPhoneNumberChange = { new ->
                        userViewModel.onUserUpdateValueChange {
                            it.copy(phoneNumber = new, phoneNumberTouched = true)
                        }
                    },
                    onAddressChange = { address ->
                        userViewModel.onUserUpdateValueChange { it.copy(address = address) }
                    },
                    onAddressTouched = {
                        userViewModel.onUserUpdateValueChange { it.copy(addressTouched = true) }
                    },
                    onSave = {
                        val userDto = UserDtos.UserPatch(
                            firstName = if (userUpdateUiState.firstNameTouched) userUpdateUiState.firstName else null,
                            lastName = if (userUpdateUiState.lastNameTouched) userUpdateUiState.lastName else null,
                            emailAddress = if (userUpdateUiState.emailAddressTouched) userUpdateUiState.emailAddress else null,
                            phoneNumber = if (userUpdateUiState.phoneNumberTouched) userUpdateUiState.phoneNumber else null,
                            address = if (userUpdateUiState.addressTouched) userUpdateUiState.address else null
                        )
                        userViewModel.updateUser(userDto)
                    },
                    onCancel = { showUpdateInputs = false }
                )
                if (!showUpdateInputs) {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Edit",
                        onClick = {
                            userViewModel.onUserUpdateValueChange {
                                it.copy(
                                    firstName = currentUserUiState.currentUser?.firstName.orEmpty(),
                                    lastName = currentUserUiState.currentUser?.lastName.orEmpty(),
                                    address = com.vfd.client.data.remote.dtos.AddressDtos.AddressCreate(
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
                AppPasswordChangeForm(
                    visible = showPasswordInputs,
                    isLoading = userUpdateUiState.isLoading,
                    fieldErrors = userUpdateUiState.fieldErrors,
                    onSubmit = { passwordDto -> userViewModel.changePassword(passwordDto) },
                    onCancel = { showPasswordInputs = false }
                )
                if (!showPasswordInputs) {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Change password",
                        onClick = { showPasswordInputs = true }
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
                        AppSearchHoursForm(
                            visible = showHourInputs,
                            errorMessage = currentFirefighterUiState.errorMessage,
                            onSubmit = { year, quarter ->
                                firefighterViewModel.getHoursForQuarter(year, quarter)
                            }
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