package com.vfd.client.ui.components.elements

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vfd.client.ui.theme.appTextFieldColors
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.Calendar
import java.time.LocalDateTime as JavaLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppDateTimePicker(
    label: String,
    selectedDateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    placeholder: String = "Select date and time"

) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val shape = RoundedCornerShape(14.dp)

    val openDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val localDateTime =
                            JavaLocalDateTime.of(year, month + 1, dayOfMonth, hour, minute)
                        onDateTimeSelected(localDateTime.toKotlinLocalDateTime())
                    },
                    hourOfDay, minute, true
                ).show()
            },
            year, month, dayOfMonth
        ).show()
    }

    OutlinedTextField(
        value = selectedDateTime?.toString() ?: "",
        onValueChange = { /* readOnly */ },
        readOnly = true,
        enabled = enabled,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        placeholder = { Text(placeholder) },
        trailingIcon = {
            IconButton(onClick = { openDatePicker() }, enabled = enabled) {
                Icon(Icons.Default.DateRange, contentDescription = null)
            }
        },
        isError = !errorMessage.isNullOrBlank(),
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { openDatePicker() },
        shape = shape,
        colors = appTextFieldColors()
    )
}