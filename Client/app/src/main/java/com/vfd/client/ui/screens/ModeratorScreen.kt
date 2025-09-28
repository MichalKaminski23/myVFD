package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppButton
import com.vfd.client.ui.components.AppCard
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.UserViewModel

@Composable
fun ModeratorScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getUserByEmailAddress()
        firefighterViewModel.getFirefighterByEmailAddress()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        currentUser.user?.let { user ->
            AppCard(
                "👤 ${user.firstName} ${user.lastName}",
                "\uD83D\uDCE7 ${user.emailAddress}" + "\n📱 ${user.phoneNumber}",
                "🏠 ${user.address.country}, ${user.address.voivodeship}, " +
                        "${user.address.street} ${user.address.houseNumber}" + "/" +
                        (user.address.apartNumber ?: "null") +
                        " ${user.address.postalCode} ${user.address.city}",
                null
            )
        } ?: CircularProgressIndicator()

        if (currentFirefighterUiState.currentFirefighter != null) {
            AppCard(
                "\uD83D\uDE92 ${currentFirefighterUiState.currentFirefighter!!.firedepartmentName}",
                "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${currentFirefighterUiState.currentFirefighter!!.role}",
                "✨ To God for glory, to people for salvation.",
                null
            )
        } else {
            CircularProgressIndicator()
        }

        Spacer(Modifier.height(24.dp))
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