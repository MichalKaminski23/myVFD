package com.vfd.client.ui.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.vfd.client.data.remote.dtos.PasswordDtos
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.texts.AppTextField

@Composable
fun AppPasswordChangeForm(
    visible: Boolean,
    isLoading: Boolean,
    fieldErrors: Map<String, String>,
    onSubmit: (PasswordDtos.PasswordChange) -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    AppTextField(
        value = currentPassword,
        onValueChange = { currentPassword = it },
        label = "Current password",
        errorMessage = fieldErrors["currentPassword"],
        visualTransformation = PasswordVisualTransformation()
    )
    AppTextField(
        value = newPassword,
        onValueChange = { newPassword = it },
        label = "New password",
        errorMessage = fieldErrors["newPassword"],
        visualTransformation = PasswordVisualTransformation()
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppButton(
            icon = Icons.Default.Check,
            label = "Save",
            onClick = { onSubmit(PasswordDtos.PasswordChange(currentPassword, newPassword)) },
            modifier = Modifier.weight(1f),
            enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && !isLoading,
            loading = isLoading
        )
        AppButton(
            icon = Icons.Default.Close,
            label = "Cancel",
            onClick = {
                onCancel()
            },
            modifier = Modifier.weight(1f),
        )
    }
}