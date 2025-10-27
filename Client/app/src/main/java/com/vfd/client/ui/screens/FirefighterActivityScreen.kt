package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterActivityDtos
import com.vfd.client.data.remote.dtos.FirefighterRole
import com.vfd.client.data.remote.dtos.FirefighterStatus
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFirefighterActivityCard
import com.vfd.client.ui.components.elements.AppDateTimePicker
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.elements.AppStringDropdown
import com.vfd.client.ui.components.globals.AppUiEvents
import com.vfd.client.ui.components.layout.AppListScreen
import com.vfd.client.ui.components.texts.AppDaysCounter
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FirefighterActivityTypeViewModel
import com.vfd.client.ui.viewmodels.FirefighterActivityViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel
import com.vfd.client.utils.RefreshEvent
import com.vfd.client.utils.RefreshManager

@Composable
fun FirefighterActivityScreen(
    firefighterActivityViewModel: FirefighterActivityViewModel,
    firefighterActivityTypeViewModel: FirefighterActivityTypeViewModel = hiltViewModel(),
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController,
    firefighterId: Int? = null,
    snackbarHostState: SnackbarHostState,
    firefighterName: String? = null,
    firefighterLastName: String? = null
) {

    val firefighterActivityUiState by firefighterActivityViewModel.firefighterActivityUiState.collectAsState()
    val firefighterActivityTypeUiState by firefighterActivityTypeViewModel.firefighterActivityTypeUiState.collectAsState()
    val firefighterActivityUpdateUiState by firefighterActivityViewModel.firefighterActivityUpdateUiState.collectAsState()
    val currentFirefighterUiState by firefighterViewModel.currentFirefighterUiState.collectAsState()
    var editingFirefighterActivityId by remember { mutableStateOf<Int?>(null) }

    val hasMore = firefighterActivityUiState.page < firefighterActivityUiState.totalPages - 1

    val filteredData = remember(firefighterActivityUiState.activities, firefighterId) {
        if (firefighterId != null) {
            firefighterActivityUiState.activities.filter { it.firefighterId == firefighterId }
        } else firefighterActivityUiState.activities
    }

    val currentRole = currentFirefighterUiState.currentFirefighter?.role
    val currentFirefighterId = currentFirefighterUiState.currentFirefighter?.firefighterId
    val isOwner = firefighterId == currentFirefighterId
    val canEditStatusAsPresident = currentRole == FirefighterRole.PRESIDENT.toString()
    var editStatusOnly by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }

    val statusPairs = FirefighterStatus.entries.map { status ->
        status.name to when (status) {
            FirefighterStatus.REJECTED -> stringResource(id = R.string.rejected)
            FirefighterStatus.PENDING -> stringResource(id = R.string.pending)
            FirefighterStatus.ACTIVE -> stringResource(id = R.string.active)
        }
    }

    val statusItems = statusPairs.map { it.second }
    val selectedStatusLabel =
        statusPairs.firstOrNull { it.first == firefighterActivityUpdateUiState.status }?.second
            ?: ""

    AppUiEvents(firefighterActivityViewModel.uiEvents, snackbarHostState)

    LaunchedEffect(firefighterActivityUpdateUiState.success) {
        if (firefighterActivityUpdateUiState.success) {
            editingFirefighterActivityId = null
            firefighterActivityViewModel.onActivityUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        firefighterActivityViewModel.getFirefightersActivities(page = 0, refresh = true)
        firefighterViewModel.getFirefighterByEmailAddress()

        RefreshManager.events.collect { event ->
            when (event) {
                is RefreshEvent.FirefighterActivityScreen -> {
                    firefighterActivityViewModel.getFirefightersActivities(page = 0, refresh = true)
                }

                else -> {}
            }
        }
    }

    AppListScreen(
        data = filteredData,
        isLoading = firefighterActivityUiState.isLoading,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        filter = { activity, query ->
            query.isBlank() ||
                    activity.firefighterActivityTypeName.contains(
                        query,
                        ignoreCase = true
                    )
        },
        hasMore = hasMore,
        onLoadMore = {
            if (hasMore && !firefighterActivityUiState.isLoading)
                firefighterActivityViewModel.getFirefightersActivities(page = firefighterActivityUiState.page + 1)
        },
        errorMessage = firefighterActivityUiState.errorMessage,
        itemKey = { it.firefighterActivityId }
    ) { activity ->
        val effectiveSelectedCode =
            firefighterActivityUpdateUiState.activityType.ifBlank {
                firefighterActivityTypeUiState.firefighterActivityTypes
                    .firstOrNull { it.name == activity.firefighterActivityTypeName }
                    ?.firefighterActivityType ?: ""
            }
        if (editingFirefighterActivityId == activity.firefighterActivityId) {
            if (editStatusOnly) {
                AppFirefighterActivityCard(
                    activity,
                    actions = {
                        AppStringDropdown(
                            label = stringResource(id = R.string.item_status),
                            items = statusItems,
                            selected = selectedStatusLabel,
                            onSelected = { newLabel ->
                                val code = statusPairs.firstOrNull { it.second == newLabel }?.first
                                    ?: newLabel
                                firefighterActivityViewModel.onActivityUpdateValueChange {
                                    it.copy(status = code, statusTouched = true)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = Icons.Default.Check,
                            enabled = !firefighterActivityUpdateUiState.isLoading
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppButton(
                                icon = Icons.Default.Check,
                                label = stringResource(id = R.string.save),
                                onClick = {
                                    val patch = FirefighterActivityDtos.FirefighterActivityPatch(
                                        status = if (firefighterActivityUpdateUiState.statusTouched) firefighterActivityUpdateUiState.status else null
                                    )
                                    firefighterActivityViewModel.updateFirefighterActivity(
                                        activity.firefighterActivityId,
                                        patch
                                    )
                                },
                                loading = firefighterActivityUpdateUiState.isLoading,
                                modifier = Modifier.weight(1f),
                            )
                            AppButton(
                                icon = Icons.Default.Close,
                                label = stringResource(id = R.string.cancel),
                                onClick = {
                                    editingFirefighterActivityId = null
                                    editStatusOnly = false
                                },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                )
            } else {
                AppFirefighterActivityCard(
                    activity,
                    actions = {
                        if (activity.status == "REJECTED" || activity.status == "PENDING") {
                            AppDropdown(
                                items = firefighterActivityTypeUiState.firefighterActivityTypes,
                                selectedCode = effectiveSelectedCode,
                                codeSelector = { it.firefighterActivityType },
                                labelSelector = { it.name },
                                label = stringResource(id = R.string.item_type),
                                onSelected = { firefighterActivityType ->
                                    firefighterActivityViewModel.onActivityUpdateValueChange {
                                        it.copy(
                                            activityType = firefighterActivityType.firefighterActivityType,
                                            activityTypeTouched = true
                                        )
                                    }
                                },
                                onLoadMore = {
                                    if (firefighterActivityTypeUiState.page + 1 < firefighterActivityTypeUiState.totalPages) {
                                        firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(
                                            page = firefighterActivityTypeUiState.page + 1
                                        )
                                    }
                                },
                                hasMore = firefighterActivityTypeUiState.page + 1 < firefighterActivityTypeUiState.totalPages,
                                onExpand = {
                                    if (firefighterActivityTypeUiState.firefighterActivityTypes.isEmpty())
                                        firefighterActivityTypeViewModel.getAllFirefighterActivityTypes(
                                            page = 0
                                        )
                                },
                                icon = Icons.Default.Build
                            )
                            AppDateTimePicker(
                                selectedDateTime = firefighterActivityUpdateUiState.activityDate,
                                onDateTimeSelected = { newDateTime ->
                                    firefighterActivityViewModel.onActivityUpdateValueChange {
                                        it.copy(
                                            activityDate = newDateTime,
                                            activityDateTouched = true
                                        )
                                    }
                                },
                                label = stringResource(id = R.string.item_date),
                            )
                            AppDateTimePicker(
                                selectedDateTime = firefighterActivityUpdateUiState.expirationDate,
                                onDateTimeSelected = { newDateTime ->
                                    firefighterActivityViewModel.onActivityUpdateValueChange {
                                        it.copy(
                                            expirationDate = newDateTime,
                                            expirationDateTouched = true
                                        )
                                    }
                                },
                                label = stringResource(id = R.string.item_end_date),
                            )
                            AppTextField(
                                value = firefighterActivityUpdateUiState.description,
                                onValueChange = { new ->
                                    firefighterActivityViewModel.onActivityUpdateValueChange() {
                                        it.copy(description = new, descriptionTouched = true)
                                    }
                                },
                                label = stringResource(id = R.string.item_description),
                                errorMessage = firefighterActivityUpdateUiState.errorMessage,
                                singleLine = false
                            )
                            if (currentRole == "PRESIDENT") {
                                AppStringDropdown(
                                    label = stringResource(id = R.string.item_status),
                                    items = statusItems,
                                    selected = selectedStatusLabel,
                                    onSelected = { newLabel ->
                                        val code =
                                            statusPairs.firstOrNull { it.second == newLabel }?.first
                                                ?: newLabel
                                        firefighterActivityViewModel.onActivityUpdateValueChange {
                                            it.copy(status = code, statusTouched = true)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    leadingIcon = Icons.Default.Check,
                                    enabled = !firefighterActivityUpdateUiState.isLoading
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                val commonValid =
                                    firefighterActivityUpdateUiState.activityDate != null &&
                                            effectiveSelectedCode.isNotBlank() &&
                                            firefighterActivityUpdateUiState.expirationDate != null &&
                                            firefighterActivityUpdateUiState.description.isNotBlank()

                                val presidentValid =
                                    firefighterActivityUpdateUiState.status.isNotBlank()

                                val saveEnabled = if (currentRole == "PRESIDENT") {
                                    !firefighterActivityUpdateUiState.isLoading && (commonValid || presidentValid)
                                } else {
                                    !firefighterActivityUpdateUiState.isLoading && commonValid
                                }
                                AppButton(
                                    icon = Icons.Default.Check,
                                    label = stringResource(id = R.string.save),
                                    onClick = {
                                        activity.firefighterActivityId.let { id ->
                                            val firefighterActivityDto =
                                                FirefighterActivityDtos.FirefighterActivityPatch(
                                                    firefighterActivityType = if (firefighterActivityUpdateUiState.activityTypeTouched)
                                                        firefighterActivityUpdateUiState.activityType
                                                    else null,

                                                    activityDate = if (firefighterActivityUpdateUiState.activityDateTouched)
                                                        firefighterActivityUpdateUiState.activityDate
                                                    else null,

                                                    expirationDate = if (firefighterActivityUpdateUiState.expirationDateTouched)
                                                        firefighterActivityUpdateUiState.expirationDate
                                                    else null,

                                                    description = if (firefighterActivityUpdateUiState.descriptionTouched)
                                                        firefighterActivityUpdateUiState.description
                                                    else null,

                                                    status = if (currentRole == "PRESIDENT" && firefighterActivityUpdateUiState.statusTouched)
                                                        firefighterActivityUpdateUiState.status
                                                    else null,
                                                )
                                            firefighterActivityViewModel.updateFirefighterActivity(
                                                id,
                                                firefighterActivityDto
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    enabled = saveEnabled,
                                    loading = firefighterActivityUpdateUiState.isLoading
                                )
                                AppButton(
                                    icon = Icons.Default.Close,
                                    label = stringResource(id = R.string.cancel),
                                    onClick = { editingFirefighterActivityId = null },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                )
                AppErrorText(
                    message = firefighterActivityUpdateUiState.errorMessage ?: ""
                )
            }
        } else {
            AppFirefighterActivityCard(
                activity,
                actions = {
                    if (isOwner) {
                        if (activity.status == "REJECTED" || activity.status == "PENDING") {
                            AppButton(
                                icon = Icons.Default.Edit,
                                label = stringResource(id = R.string.edit),
                                onClick = {
                                    editingFirefighterActivityId = activity.firefighterActivityId
                                    val preselectedCode =
                                        firefighterActivityTypeUiState.firefighterActivityTypes
                                            .firstOrNull { it.name == activity.firefighterActivityTypeName }
                                            ?.firefighterActivityType ?: ""
                                    firefighterActivityViewModel.onActivityUpdateValueChange() {
                                        it.copy(
                                            activityType = preselectedCode,
                                            activityDate = activity.activityDate,
                                            expirationDate = activity.expirationDate,
                                            description = activity.description ?: "",
                                            status = activity.status,
                                            activityTypeTouched = false,
                                            activityDateTouched = false,
                                            expirationDateTouched = false,
                                            descriptionTouched = false,
                                            statusTouched = false,
                                            errorMessage = null,
                                            isLoading = false,
                                            success = false

                                        )
                                    }
                                    editStatusOnly = false
                                }
                            )
                        }
                    } else if (canEditStatusAsPresident) {
                        AppButton(
                            icon = Icons.Default.Edit,
                            label = stringResource(id = R.string.edit),
                            onClick = {
                                editingFirefighterActivityId = activity.firefighterActivityId
                                firefighterActivityViewModel.onActivityUpdateValueChange() {
                                    it.copy(
                                        status = activity.status,
                                        statusTouched = false,
                                        errorMessage = null,
                                        isLoading = false,
                                        success = false
                                    )
                                }
                                editStatusOnly = true
                            }
                        )
                    }
                    AppDaysCounter(
                        ourDate = activity.expirationDate
                    )
                }
            )
        }
    }
}