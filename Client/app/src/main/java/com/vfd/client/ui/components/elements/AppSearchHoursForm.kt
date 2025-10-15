package com.vfd.client.ui.components.elements

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.texts.AppTextField

@Composable
fun AppSearchHoursForm(
    visible: Boolean,
    errorMessage: String? = null,
    onSubmit: (Int, Int) -> Unit,
) {
    if (!visible) return

    var quarterInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }

    AppTextField(
        value = quarterInput,
        onValueChange = { raw ->
            val digits = raw.filter(Char::isDigit).take(1)
            if (digits.isEmpty() || digits in listOf("1", "2", "3", "4")) {
                quarterInput = digits
            }
        },
        label = "Quarter (1-4)",
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    AppTextField(
        value = yearInput,
        onValueChange = { raw ->
            yearInput = raw.filter(Char::isDigit).take(4)
        },
        label = "Year (YYYY)",
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    AppButton(
        icon = Icons.Default.ThumbUp,
        label = "Show hours from quarter",
        onClick = {
            val quarter = quarterInput.toIntOrNull() ?: 0
            val year = yearInput.toIntOrNull() ?: 0
            onSubmit(year, quarter)
        },
        enabled = quarterInput.isNotEmpty() && yearInput.isNotEmpty()
    )

}