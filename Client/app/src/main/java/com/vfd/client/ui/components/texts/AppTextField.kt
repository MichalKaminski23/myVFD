package com.vfd.client.ui.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.theme.appTextFieldColors

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    AppColumn(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    fontSize = 14.sp,
                )
            },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            colors = appTextFieldColors(),
            keyboardOptions = keyboardOptions
        )
        if (errorMessage != null) {
            AppErrorText(errorMessage)
        }
    }
}