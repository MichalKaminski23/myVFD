package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vfd.client.data.remote.dtos.UserDtos
import com.vfd.client.ui.components.AppDrawer
import com.vfd.client.ui.components.BaseCard
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.ApiResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.users.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                selectedRoute = "users",
                onNavigate = { route ->
                    scope.launch { drawerState.close() }
                    navController.navigate(route)
                }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Users") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.loadAllUsers() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Odśwież"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (val result = state) {
                    is ApiResult.Loading -> {
                        CircularProgressIndicator()
                    }

                    is ApiResult.Success -> {
                        val pageResponse = result.data
                        val users = pageResponse?.items ?: emptyList()
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

                    is ApiResult.Error -> {
                        Text("Error: ${result.message}", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserDtos.UserResponse, viewModel: UserViewModel = hiltViewModel()) {
    BaseCard(
        title = "${user.firstName} ${user.lastName}",
        subtitle = "Email: ${user.emailAddress}",
        description = "Phone: ${user.phoneNumber}\nAddress: ${user.address.city}, ${user.address.street}",
        status = if (user.active) "Active" else "Inactive",
        statusColor = if (user.active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
        actions = {
            EditUserNameScreen(viewModel, user.userId)
        }
    )
}

@Composable
fun EditUserNameScreen(viewModel: UserViewModel, userId: Int) {
    val name by viewModel.name.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Imię") },
            isError = errorMessage != null // podświetli TextField na czerwono jeśli jest błąd
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.updateUserName(userId) }) {
            Text("Zapisz zmiany")
        }
    }
}

