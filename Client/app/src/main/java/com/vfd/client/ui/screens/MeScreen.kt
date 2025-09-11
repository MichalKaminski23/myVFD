package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val token by authViewModel.token.collectAsState()
    val user by userViewModel.user.collectAsState()

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            kotlinx.coroutines.delay(200)
            userViewModel.getMyself()
        }
    }

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("My Profile") },
//                actions = {
//                    IconButton(onClick = { userViewModel.getMyself() }) {
//                        Icon(
//                            imageVector = Icons.Default.Refresh,
//                            contentDescription = "Refresh"
//                        )
//                    }
//                }
//            )
//        }
//    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(24.dp))

        if (user != null) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    "👤 ${user!!.firstName} ${user!!.lastName}",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))
                Text("📧 ${user!!.emailAddress}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("📱 ${user!!.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    "🏠 ${user!!.address.street} ${user!!.address.houseNumber}, " +
                            (user!!.address.apartNumber ?: "") + "\n" +
                            "${user!!.address.postalCode} ${user!!.address.city}, ${user!!.address.country}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            CircularProgressIndicator()
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

