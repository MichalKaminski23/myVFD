package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.BaseCard
import com.vfd.client.ui.components.GeneralDropdown
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    firedepartmentViewModel: FiredepartmentViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {
    val token by authViewModel.token.collectAsState()
    val currentUser by userViewModel.user.collectAsState()
    val firedepartmentUiState by firedepartmentViewModel.firedepartmentUiState.collectAsState()
    var selectedFiredepartmentId by rememberSaveable { mutableStateOf<Int?>(null) }
    val firefighterUiState by firefighterViewModel.firefighterUiState.collectAsState()
    val currentFirefighter by firefighterViewModel.firefighter.collectAsState()

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            userViewModel.getCurrentUser()
            firefighterViewModel.getCurrentFirefighter()
            if (firefighterViewModel.firefighter.value == null) {
                firedepartmentViewModel.getAllFiredepartments()
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
        Spacer(Modifier.height(24.dp))

        if (currentUser != null) {
            BaseCard(
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

        when {
            currentFirefighter != null -> {
                when (currentFirefighter!!.status) {

                    FirefighterStatus.PENDING -> {
                        Text(
                            "Your request has been sent. Wait for moderator approval.",
                            textAlign = TextAlign.Center
                        )
                    }

                    FirefighterStatus.ACTIVE -> {
                        BaseCard(
                            "\uD83D\uDE92 ${currentFirefighter!!.firedepartmentName}",
                            "\uD83D\uDC65 Status: ${currentFirefighter!!.status}",
                            "\uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${currentFirefighter!!.role}",
                            null
                        )
                    }
                }
            }

            firefighterUiState.loading -> {
                CircularProgressIndicator()
            }

            firefighterUiState.error != null -> {
                Text(
                    text = firefighterUiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 2.dp)
                )
            }

            firefighterUiState.success -> {
                Text(
                    "Application sent successfully. Wait for moderator approval.",
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                GeneralDropdown(
                    items = firedepartmentUiState.firedepartments,
                    selectedId = selectedFiredepartmentId,
                    idSelector = { it.firedepartmentId },
                    labelSelector = { it.name },
                    label = "Choose firedepartment",
                    onSelected = { firedepartment ->
                        selectedFiredepartmentId = firedepartment.firedepartmentId
                    }
                )

                Button(
                    onClick = {
                        val userId = currentUser?.userId
                        val firedepartmentId = selectedFiredepartmentId
                        if (userId != null && firedepartmentId != null) {
                            firefighterViewModel.crateFirefighter(userId, firedepartmentId)
                        }
                    },
                    enabled = selectedFiredepartmentId != null,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Text("Send application to VFD's moderator.")
                }

            }
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("welcomeScreen") {
                    popUpTo("meScreen") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}