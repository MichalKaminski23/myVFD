package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterCard
import com.vfd.client.ui.components.cards.AppUserCard
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.globals.AppLoadingBar
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.viewmodels.AuthViewModel
import com.vfd.client.ui.viewmodels.FiredepartmentUiState
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.ui.viewmodels.UserViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun MeScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    firedepartmentViewModel: FiredepartmentViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val token by authViewModel.token.collectAsState()

    val currentUserUiState by userViewModel.currentUserUiState.collectAsState()

    val firedepartmentUiState by firedepartmentViewModel.firedepartmentUiState.collectAsState()
    var selectedFiredepartmentId by rememberSaveable { mutableStateOf<Int?>(null) }

    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()

    AppUiEvents(firefighterViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(token) {
        if (!token.isNullOrBlank()) {
            userViewModel.getUserByEmailAddress()
            firefighterViewModel.getFirefighterByEmailAddress()
        }
    }

    LaunchedEffect(Unit) {
        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.MeScreen -> {
                    userViewModel.getUserByEmailAddress()
                    firefighterViewModel.getFirefighterByEmailAddress()
                }

                else -> {}
            }
        }
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        if (currentUserUiState.isLoading) {
            AppText(
                "The data is loading...",
                style = MaterialTheme.typography.headlineLarge
            )
        }

        AppLoadingBar(currentUserUiState.isLoading)
        AppLoadingBar(currentFirefighterUiState.isLoading)

        currentUserUiState.currentUser?.let { AppUserCard(it) }

        currentUserUiState.errorMessage?.let { AppErrorText(it) }

        if (!currentFirefighterUiState.isLoading) {
            FirefighterSection(
                firefighter = currentFirefighterUiState.currentFirefighter,
                firedepartmentUiState = firedepartmentUiState,
                selectedFiredepartmentId = selectedFiredepartmentId,
                onSelected = { selectedFiredepartmentId = it },
                onApply = { userId, firedepartmentId ->
                    if (currentFirefighterUiState.currentFirefighter?.status == FirefighterStatus.REJECTED.toString()) {
                        firefighterViewModel.changeFirefighterRoleOrStatus(
                            userId,
                            FirefighterDtos.FirefighterPatch(
                                role = FirefighterRole.USER.toString(),
                                status = FirefighterStatus.PENDING.toString()
                            )
                        )
                    } else {

                        val firefighterDto = FirefighterDtos.FirefighterCreate(
                            userId = userId,
                            firedepartmentId = firedepartmentId
                        )
                        firefighterViewModel.createFirefighter(firefighterDto)
                    }
                },
                currentUserId = currentUserUiState.currentUser?.userId,
                firedepartmentViewModel = firedepartmentViewModel
            )
        }

        AppButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            label = "Logout",
            onClick = {
                authViewModel.logout()
                navController.navigate("welcomeScreen") {
                    popUpTo("meScreen") { inclusive = true }
                }
            },
            fullWidth = true,
        )
    }
}

@Composable
private fun FirefighterSection(
    firefighter: FirefighterDtos.FirefighterResponse?,
    firedepartmentUiState: FiredepartmentUiState,
    firedepartmentViewModel: FiredepartmentViewModel,
    selectedFiredepartmentId: Int?,
    onSelected: (Int) -> Unit,
    onApply: (userId: Int, deptId: Int) -> Unit,
    currentUserId: Int?
) {
    when (firefighter?.status) {
        FirefighterStatus.PENDING.toString() -> {
            Text(
                "Your request has been sent. Wait for moderator approval.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }

        FirefighterStatus.ACTIVE.toString() -> {
            AppFirefighterCard(firefighter)
        }

        FirefighterStatus.REJECTED.toString(),
        null -> {
            if (firefighter?.status == FirefighterStatus.REJECTED.toString()) {
                Text(
                    "Your previous application was rejected. You can try again.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
            AppDropdown(
                items = firedepartmentUiState.firedepartments,
                selectedId = selectedFiredepartmentId,
                idSelector = { it.firedepartmentId },
                labelSelector = { it.name },
                label = "Choose firedepartment",
                onSelected = { onSelected(it.firedepartmentId) },
                onLoadMore = {
                    if (firedepartmentUiState.page + 1 < firedepartmentUiState.totalPages) {
                        firedepartmentViewModel.getFiredepartmentsShort(
                            page = firedepartmentUiState.page + 1
                        )
                    }
                },
                hasMore = firedepartmentUiState.page + 1 < firedepartmentUiState.totalPages,
                onExpand = {
                    if (firedepartmentUiState.firedepartments.isEmpty())
                        firedepartmentViewModel.getFiredepartmentsShort(page = 0)
                },
                icon = Icons.Default.Home,
                isLoading = firedepartmentUiState.isLoading
            )
            AppButton(
                icon = Icons.AutoMirrored.Filled.Send,
                label = "Send application to VFD's moderator.",
                onClick = {
                    val deptId = selectedFiredepartmentId
                    if (currentUserId != null && deptId != null) {
                        onApply(currentUserId, deptId)
                    }
                },
                fullWidth = true,
                enabled = selectedFiredepartmentId != null,
            )
        }
    }
}