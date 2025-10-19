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
    val createState by firedepartmentViewModel.firedepartmentCreateUiState.collectAsState()
    val listState by firedepartmentViewModel.firedepartmentsUiState.collectAsState()
    val firedepartmentUpdate by firedepartmentViewModel.firedepartmentUpdateUiState.collectAsState()

    var editingFiredepartmentId by remember { mutableStateOf<Int?>(null) }

    var mode by remember { mutableStateOf("create") }

    LaunchedEffect(firedepartmentUpdate.success) {
        if (firedepartmentUpdate.success) {
            editingFiredepartmentId = null
            firedepartmentViewModel.onFiredepartmentUpdateValueChange { it.copy(success = false) }
        }
    }

    LaunchedEffect(Unit) {
        !listState.isLoading
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
                    value = createState.name,
                    onValueChange = { new ->
                        firedepartmentViewModel.onFiredepartmentCreateValueChange { it.copy(name = new) }
                    },
                    label = "Name",
                    errorMessage = createState.errorMessage
                )

                AppAddressActions(
                    address = createState.address,
                    errors = createState.fieldErrors,
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
                    selected = createState.nrfs.toString(),
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
                                name = createState.name,
                                address = AddressDtos.AddressCreate(
                                    country = createState.address.country,
                                    voivodeship = createState.address.voivodeship,
                                    city = createState.address.city,
                                    postalCode = createState.address.postalCode,
                                    street = createState.address.street,
                                    houseNumber = createState.address.houseNumber,
                                    apartNumber = createState.address.apartNumber
                                ),
                                nrfs = createState.nrfs
                            )
                        )
                    },
                    fullWidth = true,
                    enabled = !createState.isLoading && createState.name.isNotBlank()
                            && createState.address.street.isNotBlank()
                            && createState.address.city.isNotBlank()
                            && createState.address.postalCode.isNotBlank()
                            && createState.address.country.isNotBlank()
                            && createState.address.voivodeship.isNotBlank()
                            && createState.address.houseNumber.isNotBlank()
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

                items(listState.firedepartments) { fd ->
                    if (editingFiredepartmentId == fd.firedepartmentId) {
                        AppFiredepartmentCard(fd, actions = {
                            AppTextField(
                                value = firedepartmentUpdate.name,
                                onValueChange = { new ->
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(name = new, nameTouched = true)
                                    }
                                },
                                label = "Name",
                                errorMessage = firedepartmentUpdate.errorMessage
                            )
                            AppAddressActions(
                                address = firedepartmentUpdate.address,
                                errors = firedepartmentUpdate.fieldErrors,
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
                                selected = firedepartmentUpdate.nrfs.toString(),
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
                                                name = if (firedepartmentUpdate.nameTouched) firedepartmentUpdate.name else null,
                                                address = if (firedepartmentUpdate.addressTouched) firedepartmentUpdate.address else null,
                                                nrfs = if (firedepartmentUpdate.nrfsTouched) firedepartmentUpdate.nrfs else null
                                            )
                                        firedepartmentViewModel.updateFiredepartment(
                                            fd.firedepartmentId, firedepartmentDto
                                        )
                                    },
                                    enabled = !firedepartmentUpdate.isLoading && firedepartmentUpdate.name.isNotBlank()
                                            && firedepartmentUpdate.address?.street?.isNotBlank() == true
                                            && firedepartmentUpdate.address?.city?.isNotBlank() == true
                                            && firedepartmentUpdate.address?.postalCode?.isNotBlank() == true
                                            && firedepartmentUpdate.address?.country?.isNotBlank() == true
                                            && firedepartmentUpdate.address?.voivodeship?.isNotBlank() == true
                                            && firedepartmentUpdate.address?.houseNumber?.isNotBlank() == true,
                                    loading = firedepartmentUpdate.isLoading,
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
                        AppFiredepartmentCard(fd, actions = {
                            AppButton(
                                icon = Icons.Filled.Edit,
                                label = "Edit",
                                onClick = {
                                    editingFiredepartmentId = fd.firedepartmentId
                                    firedepartmentViewModel.onFiredepartmentUpdateValueChange {
                                        it.copy(
                                            name = fd.name,
                                            address = AddressDtos.AddressCreate(
                                                country = fd.address.country,
                                                voivodeship = fd.address.voivodeship,
                                                city = fd.address.city,
                                                postalCode = fd.address.postalCode,
                                                street = fd.address.street,
                                                houseNumber = fd.address.houseNumber,
                                                apartNumber = fd.address.apartNumber
                                            ),
                                            nrfs = fd.nrfs,
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
                            if (!listState.isLoading && listState.page + 1 < listState.totalPages) {
                                firedepartmentViewModel.getFiredepartmentsShort(
                                    page = listState.page + 1,
                                    size = 20
                                )
                            }
                        },
                        fullWidth = true,
                        enabled = !listState.isLoading && listState.page + 1 < listState.totalPages
                    )
                }
            }
        }
    }
}