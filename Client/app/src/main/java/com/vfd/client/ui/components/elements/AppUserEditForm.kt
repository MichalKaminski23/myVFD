package com.vfd.client.ui.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vfd.client.data.remote.dtos.AddressDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.texts.AppTextField
import com.vfd.client.ui.viewmodels.UserUpdateUiState

@Composable
fun AppUserEditForm(
    state: UserUpdateUiState,
    visible: Boolean,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailAddressChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAddressChange: (AddressDtos.AddressCreate) -> Unit,
    onAddressTouched: () -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    if (!visible) return

    AppTextField(
        value = state.firstName,
        onValueChange = onFirstNameChange,
        label = "First name",
        errorMessage = state.fieldErrors["firstName"]
    )
    AppTextField(
        value = state.lastName,
        onValueChange = onLastNameChange,
        label = "Last name",
        errorMessage = state.fieldErrors["lastName"]
    )
    AppTextField(
        value = state.emailAddress,
        onValueChange = onEmailAddressChange,
        label = "Email address",
        errorMessage = state.fieldErrors["emailAddress"]
    )
    AppTextField(
        value = state.phoneNumber,
        onValueChange = onPhoneNumberChange,
        label = "Phone number",
        errorMessage = state.fieldErrors["phoneNumber"],
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
    )
    AppAddressActions(
        address = state.address,
        errors = state.fieldErrors,
        onAddressChange = onAddressChange,
        onTouched = onAddressTouched
    )

    val saveEnabled =
        state.firstName.isNotBlank() &&
                state.lastName.isNotBlank() &&
                state.emailAddress.isNotBlank() &&
                state.phoneNumber.isNotBlank() &&
                state.address.country.isNotBlank() &&
                state.address.voivodeship.isNotBlank() &&
                state.address.city.isNotBlank() &&
                state.address.postalCode.isNotBlank() &&
                state.address.street.isNotBlank() &&
                state.address.houseNumber.isNotBlank() &&
                !state.isLoading

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppButton(
            icon = Icons.Default.Check,
            label = "Save",
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = saveEnabled,
            loading = state.isLoading
        )
        AppButton(
            icon = Icons.Default.Close,
            label = "Cancel",
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        )
    }
}