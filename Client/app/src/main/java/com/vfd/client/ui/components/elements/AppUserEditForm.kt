package com.vfd.client.ui.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vfd.client.R
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
        label = stringResource(id = R.string.first_name),
        errorMessage = state.fieldErrors["firstName"]
    )
    AppTextField(
        value = state.lastName,
        onValueChange = onLastNameChange,
        label = stringResource(id = R.string.last_name),
        errorMessage = state.fieldErrors["lastName"]
    )
    AppTextField(
        value = state.emailAddress,
        onValueChange = onEmailAddressChange,
        label = stringResource(id = R.string.email_address),
        errorMessage = state.fieldErrors["emailAddress"]
    )
    AppTextField(
        value = state.phoneNumber,
        onValueChange = onPhoneNumberChange,
        label = stringResource(id = R.string.phone_number),
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
            label = stringResource(id = R.string.save),
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = saveEnabled,
            loading = state.isLoading
        )
        AppButton(
            icon = Icons.Default.Close,
            label = stringResource(id = R.string.cancel),
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        )
    }
}