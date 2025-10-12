package com.vfd.client.ui.components.texts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vfd.client.utils.daysUntilSomething
import kotlinx.datetime.LocalDateTime
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppDaysCounter(
    ourDate: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    val daysLeft = daysUntilSomething(ourDate)
    val today = LocalDate.now()

    if (daysLeft > 0) {
        AppText(
            text = "$daysLeft days left from $today",
            color = if (daysLeft <= 7) Color.Red else Color.LightGray,
            style = if (daysLeft <= 7) MaterialTheme.typography.displayMedium else MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else if (daysLeft == -1L) {
        AppText(
            text = "Expiration date is not specified",
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else {
        AppText(
            text = "Date passed",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    }
}