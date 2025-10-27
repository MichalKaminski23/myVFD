package com.vfd.client.ui.components.texts

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.utils.daysUntilSomething
import kotlinx.datetime.LocalDateTime
import java.time.LocalDate

@Composable
fun AppDaysCounter(
    ourDate: LocalDateTime?,
    modifier: Modifier = Modifier
) {
    val daysLeft = daysUntilSomething(ourDate)
    val today = LocalDate.now()

    if (daysLeft > 0) {
        AppText(
            text = "$daysLeft ${stringResource(id = R.string.days_left_from)}: $today",
            color = if (daysLeft <= 7) Color.Red else Color.LightGray,
            style = if (daysLeft <= 7) MaterialTheme.typography.displayMedium else MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else if (daysLeft == -1L) {
        AppText(
            text = stringResource(id = R.string.end_date_not_set),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    } else {
        AppText(
            text = stringResource(id = R.string.date_passed),
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
        )
    }
}