package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.data.remote.dtos.FiredepartmentDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.cards.AppFiredepartmentCard
import com.vfd.client.ui.components.elements.AppAddressActions
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppStringDropdown
import com.vfd.client.ui.components.texts.AppText
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.FiredepartmentViewModel

@Composable
fun FiredepartmentsHub(
    firedepartmentViewModel: FiredepartmentViewModel
) {

    val firedepartmentsUiState by firedepartmentViewModel.firedepartmentsUiState.collectAsState()
    val firedepartmentCreateUiState by firedepartmentViewModel.firedepartmentCreateUiState.collectAsState()
    val firedepartmentUpdateUiState by firedepartmentViewModel.firedepartmentUpdateUiState.collectAsState()

    var editingFiredepartmentId by remember { mutableStateOf<Int?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(firedepartmentUpdateUiState.success) {
        if (firedepartmentUpdateUiState.success) {
            editingFiredepartmentId = null
            firedepartmentViewModel.onFiredepartmentUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        !firedepartmentsUiState.isLoading
    }

    LaunchedEffect(mode) {
        if (mode == "edit") {
            firedepartmentViewModel.getFiredepartments(page = 0, size = 20)
        }
    }

    AppColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (mode == "create") {
            AppColumn(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Create",
                        onClick = { mode = "create" },
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Edit",
                        onClick = { mode = "edit" },
                        modifier = Modifier.weight(1f)
                    )
                }
                AppText("New firedepartment", style = MaterialTheme.typography.headlineSmall)

                AppTextField(
                    value = firedepartmentCreateUiState.name,
                    onValueChange = { new ->
                        firedepartmentViewModel.onFiredepartmentCreateValueChange { it.copy(name = new) }
                    },
                    label = "Name",
                    errorMessage = firedepartmentCreateUiState.errorMessage
                )

                AppAddressActions(
                    address = firedepartmentCreateUiState.address,
                    errors = firedepartmentCreateUiState.fieldErrors,
                    onAddressChange = { newAddress ->
                        firedepartmentViewModel.onFiredepartmentCreateValueChange { it.copy(address = newAddress) }
                    },
                    onTouched = {
                        firedepartmentViewModel.onFiredepartmentCreateValueChange {
                            it.copy(
                                addressTouched = true
                            )
                        }
                    }
                )

                AppStringDropdown(
                    label = "NRFS",
                    items = listOf(true, false).map { it.toString() },
                    selected = firedepartmentCreateUiState.nrfs.toString(),
                    onSelected = { new ->
                        firedepartmentViewModel.onFiredepartmentCreateValueChange { it.copy(nrfs = new == "true") }
                    },
                    leadingIcon = Icons.AutoMirrored.Filled.ArrowBack
                )

                AppButton(
                    icon = Icons.Filled.Edit,
                    label = "Save",
                    onClick = {
                        firedepartmentViewModel.createFiredepartment(
                            FiredepartmentDtos.FiredepartmentCreate(
                                name = firedepartmentCreateUiState.name,
                                address = AddressDtos.AddressCreate(
                                    country = firedepartmentCreateUiState.address.country,
                                    voivodeship = firedepartmentCreateUiState.address.voivodeship,
                                    city = firedepartmentCreateUiState.address.city,
                                    postalCode = firedepartmentCreateUiState.address.postalCode,
                                    street = firedepartmentCreateUiState.address.street,
                                    houseNumber = firedepartmentCreateUiState.address.houseNumber,
                                    apartNumber = firedepartmentCreateUiState.address.apartNumber
                                ),
                                nrfs = firedepartmentCreateUiState.nrfs
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !firedepartmentCreateUiState.isLoading && firedepartmentCreateUiState.name.isNotBlank()
                            && firedepartmentCreateUiState.address.street.isNotBlank()
                            && firedepartmentCreateUiState.address.city.isNotBlank()
                            && firedepartmentCreateUiState.address.postalCode.isNotBlank()
                            && firedepartmentCreateUiState.address.country.isNotBlank()
                            && firedepartmentCreateUiState.address.voivodeship.isNotBlank()
                            && firedepartmentCreateUiState.address.houseNumber.isNotBlank()
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AppButton(
                            icon = Icons.Filled.Edit,
                            label = "Create",
                            onClick = { mode = "create" },
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            icon = Icons.Filled.Edit,
                            label = "Edit",
                            onClick = { mode = "edit" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    AppText("Edit firedepartment", style = MaterialTheme.typography.headlineSmall)
                }

                items(firedepartmentsUiState.firedepartments) { firedepartment ->
                    if (editingFiredepartmentId == firedepartment.firedepartmentId) {
                        AppFiredepartmentCard(firedepartment, actions = {
                            AppTextField(
                                value = firedepartmentUpdateUiState.name,
                                onValueChange = { new ->
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = "Name",
                                errorMessage = firedepartmentUpdateUiState.errorMessage
                            )
                            AppAddressActions(
                                address = firedepartmentUpdateUiState.address,
                                errors = firedepartmentUpdateUiState.fieldErrors,
                                onAddressChange = { newAddress ->
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(address = newAddress)
                                    }
                                },
                                onTouched = {
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(
                                            addressTouched = true
                                        )
                                    }
                                }
                            )
                            AppStringDropdown(
                                label = "NRFS",
                                items = listOf(true, false).map { it.toString() },
                                selected = firedepartmentUpdateUiState.nrfs.toString(),
                                onSelected = { new ->
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(nrfs = new == "true", nrfsTouched = true)
                                    }
                                },
                                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Save",
                                    onClick = {
                                        val firedepartmentDto =
                                            FiredepartmentDtos.FiredepartmentPatch(
                                                name = if (firedepartmentUpdateUiState.nameTouched) firedepartmentUpdateUiState.name else null,
                                                address = if (firedepartmentUpdateUiState.addressTouched) firedepartmentUpdateUiState.address else null,
                                                nrfs = if (firedepartmentUpdateUiState.nrfsTouched) firedepartmentUpdateUiState.nrfs else null
                                            )
                                        firedepartmentViewModel.updateFiredepartment(
                                            firedepartment.firedepartmentId, firedepartmentDto
                                        )
                                    },
                                    enabled = !firedepartmentUpdateUiState.isLoading && firedepartmentUpdateUiState.name.isNotBlank()
                                            && firedepartmentUpdateUiState.address?.street?.isNotBlank() == true
                                            && firedepartmentUpdateUiState.address?.city?.isNotBlank() == true
                                            && firedepartmentUpdateUiState.address?.postalCode?.isNotBlank() == true
                                            && firedepartmentUpdateUiState.address?.country?.isNotBlank() == true
                                            && firedepartmentUpdateUiState.address?.voivodeship?.isNotBlank() == true
                                            && firedepartmentUpdateUiState.address?.houseNumber?.isNotBlank() == true,
                                    loading = firedepartmentUpdateUiState.isLoading,
                                    modifier = Modifier.weight(1f),
                                )
                                AppButton(
                                    icon = Icons.Filled.Edit,
                                    label = "Cancel",
                                    onClick = {
                                        editingFiredepartmentId = null
                                        firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                            it.copy(
                                                success = false
                                            )
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        })
                    } else {
                        AppFiredepartmentCard(firedepartment, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = "Edit",
                                onClick = {
                                    editingFiredepartmentId = firedepartment.firedepartmentId
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(
                                            name = firedepartment.name,
                                            address = AddressDtos.AddressCreate(
                                                country = firedepartment.address.country,
                                                voivodeship = firedepartment.address.voivodeship,
                                                city = firedepartment.address.city,
                                                postalCode = firedepartment.address.postalCode,
                                                street = firedepartment.address.street,
                                                houseNumber = firedepartment.address.houseNumber,
                                                apartNumber = firedepartment.address.apartNumber
                                            ),
                                            nrfs = firedepartment.nrfs,
                                            nameTouched = false,
                                            nrfsTouched = false,
                                            addressTouched = false,
                                            fieldErrors = emptyMap(),
                                            errorMessage = null,
                                            success = false,
                                            isLoading = false
                                        )
                                    }
                                }
                            )
                        })
                    }
                }
                item {
                    AppButton(
                        icon = Icons.Filled.Edit,
                        label = "Load more",
                        onClick = {
                            if (!firedepartmentsUiState.isLoading && firedepartmentsUiState.page + 1 < firedepartmentsUiState.totalPages) {
                                firedepartmentViewModel.getFiredepartmentsShort(
                                    page = firedepartmentsUiState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !firedepartmentsUiState.isLoading && firedepartmentsUiState.page + 1 < firedepartmentsUiState.totalPages
                    )
                }
            }
        }
    }
}