package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.ui.vievmodels.UserViewModel
import com.vfd.client.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.users.collectAsState()

    // pierwszy raz odpalamy pobranie użytkowników
    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loadAllUsers() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Odśwież")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val result = state) {
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val users = result.data ?: emptyList()
                    if (users.isEmpty()) {
                        Text("No users found")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(users) { user ->
                                UserCard(user)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    Text("Error: ${result.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserDtos.UserResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Email: ${user.emailAddress}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Phone: ${user.phoneNumber}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (user.active) "Active" else "Inactive",
                style = MaterialTheme.typography.labelMedium,
                color = if (user.active) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Address: ${user.address.street}, ${user.address.city}, ${user.address.country} ${user.address.postalCode}",
                style = MaterialTheme.typography.bodyMedium
            )
            EditUserNameScreen(viewModel = hiltViewModel(), userId = user.userId)
        }
    }
}

@Composable
fun EditUserNameScreen(viewModel: UserViewModel, userId: Int) {
    val name by viewModel.name.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Imię") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.updateUserName(userId) }) {
            Text("Zapisz zmiany")
        }
    }
}
