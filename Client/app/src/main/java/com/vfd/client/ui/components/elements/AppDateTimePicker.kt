package com.vfd.client.ui.components.elements

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vfd.client.ui.components.buttons.AppButton
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.Calendar
import java.time.LocalDateTime as JavaLocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppDateTimePicker(
    selectedDateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime?) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

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

    AppButton(
        label = selectedDateTime?.toString() ?: "Pick date & time",
        icon = Icons.Default.DateRange,
        onClick = { openDatePicker() },
        modifier = Modifier.fillMaxWidth()
    )
}
