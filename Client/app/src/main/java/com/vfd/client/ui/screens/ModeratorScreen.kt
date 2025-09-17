package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    val currentUser by userViewModel.user.collectAsState()
    val currentFirefighter by firefighterViewModel.firefighter.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.getCurrentUser()
        firefighterViewModel.getCurrentFirefighter()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (currentUser != null) {
            AppCard(
                "👤 ${currentUser!!.firstName} ${currentUser!!.lastName}",
                "\uD83D\uDCE7 ${currentUser!!.emailAddress}" + "\n📱 ${currentUser!!.phoneNumber}",
                "🏠 ${currentUser!!.address.country}, ${currentUser!!.address.voivodeship}, " +
                        "${currentUser!!.address.street} ${currentUser!!.address.houseNumber}" + "/" +
                        (currentUser!!.address.apartNumber ?: "null") +
                        " ${currentUser!!.address.postalCode} ${currentUser!!.address.city}",
                null
            )
        } else {
            CircularProgressIndicator()
        }

        if (currentFirefighter != null) {
            AppCard(
                "\uD83D\uDE92 ${currentFirefighter!!.firedepartmentName}",
                "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${currentFirefighter!!.role}",
                "✨ To God for glory, to people for salvation.",
                null
            )
        } else {
            CircularProgressIndicator()
        }

        Spacer(Modifier.height(24.dp))
        AppButton(
            icon = Icons.Filled.ArrowBack,
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