package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.data.remote.dtos.FirefighterDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppDropdown
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun PresidentsHub(
    firefighterViewModel: FirefighterViewModel,
    firedepartmentViewModel: FiredepartmentViewModel
) {
    val firefighterCreateUiState =
        firefighterViewModel.firefighterCreateUiState.collectAsState().value

    val firedepartmentsShortUiState by firedepartmentViewModel.firedepartmentsShortUiState.collectAsState()
    var selectedFiredepartmentId by rememberSaveable { mutableStateOf<Int?>(null) }

    AppColumn(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        AppText(
            stringResource(id = R.string.president_create),
            style = MaterialTheme.typography.headlineSmall
        )

        AppTextField(
            value = firefighterCreateUiState.emailAddress,
            onValueChange = { new ->
                firefighterViewModel.onFirefighterCreateValueChange { it.copy(emailAddress = new) }
            },
            label = stringResource(id = R.string.email_address),
            errorMessage = firefighterCreateUiState.errorMessage
        )
        AppDropdown(
            items = firedepartmentsShortUiState.firedepartmentsShort,
            selectedId = selectedFiredepartmentId,
            idSelector = { it.firedepartmentId },
            labelSelector = { it.name },
            label = stringResource(id = R.string.firedepartments),
            onSelected = { selectedFiredepartmentId = it.firedepartmentId },
            onLoadMore = {
                if (firedepartmentsShortUiState.page + 1 < firedepartmentsShortUiState.totalPages) {
                    firedepartmentViewModel.getFiredepartmentsShort(
                        page = firedepartmentsShortUiState.page + 1
                    )
                }
            },
            hasMore = firedepartmentsShortUiState.page + 1 < firedepartmentsShortUiState.totalPages,
            onExpand = {
                if (firedepartmentsShortUiState.firedepartmentsShort.isEmpty())
                    firedepartmentViewModel.getFiredepartmentsShort(page = 0)
            },
            icon = Icons.Default.Home,
            isLoading = firedepartmentsShortUiState.isLoading
        )
        AppButton(
            icon = Icons.AutoMirrored.Filled.Send,
            label = stringResource(id = R.string.president_create),
            onClick = {
                val firefighterDto: FirefighterDtos.FirefighterCreateByEmailAddress =
                    FirefighterDtos.FirefighterCreateByEmailAddress(
                        userEmailAddress = firefighterCreateUiState.emailAddress,
                        firedepartmentId = selectedFiredepartmentId!!
                    )
                firefighterViewModel.createFirefighterByEmailAddress(firefighterDto)
            },
            fullWidth = true,
            enabled = selectedFiredepartmentId != null && firefighterCreateUiState.emailAddress.isNotBlank() && !firefighterCreateUiState.isLoading,
            loading = firefighterCreateUiState.isLoading
        )
    }
}