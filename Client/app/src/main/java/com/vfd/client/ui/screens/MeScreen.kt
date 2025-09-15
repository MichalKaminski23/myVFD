package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            kotlinx.coroutines.delay(200)
            userViewModel.getMyself()
            firedepartmentViewModel.getAllFiredepartments()
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
            firefighterUiState.success -> {
                Text("Application sent successfully.")
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
                    enabled = !firefighterUiState.loading && selectedFiredepartmentId != null,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    if (firefighterUiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Send application to VFD's moderator.")
                    }
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