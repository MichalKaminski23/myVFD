package com.vfd.client.ui.components.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppErrorText
import com.vfd.client.ui.components.texts.AppText

@Composable
fun AppFormDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    title: String,
    confirmLabel: String = "Save",
    cancelLabel: String = "Cancel",
    confirmEnabled: Boolean,
    confirmLoading: Boolean = false,
    errorMessage: String? = null,
    onConfirm: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!show) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            )
        ) {
            AppColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AppText(
                    title,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(12.dp))

                content()

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppButton(
                        icon = Icons.Default.Check,
                        label = confirmLabel,
                        enabled = confirmEnabled,
                        loading = confirmLoading,
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        icon = Icons.Default.Close,
                        label = cancelLabel,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (errorMessage != null) {
                    AppErrorText(errorMessage)
                }
            }
        }
    }
}

