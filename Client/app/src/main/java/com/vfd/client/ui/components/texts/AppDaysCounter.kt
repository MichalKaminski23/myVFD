package com.vfd.client.ui.components.texts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.vfd.client.utils.daysUntilSomething
import kotlinx.datetime.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppDaysCounter(
    ourDate: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    val daysLeft = daysUntilSomething(ourDate)

    if (daysLeft > 0) {
        AppText(
            text = "$daysLeft days left",
            color = if (daysLeft <= 7) Color.Red else Color.LightGray,
            style = if (daysLeft <= 7) MaterialTheme.typography.displayMedium else MaterialTheme.typography.bodyMedium,
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