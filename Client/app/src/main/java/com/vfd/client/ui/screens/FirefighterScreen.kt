package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppCard
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.viewmodels.FirefighterViewModel

@Composable
fun FirefighterScreen(
    firefighterViewModel: FirefighterViewModel = hiltViewModel(),
    navController: NavController
) {

    val firefightersFromMyFiredepartment by firefighterViewModel.firefightersFromMyFiredepartment.collectAsState()

    LaunchedEffect(Unit) {
        firefighterViewModel.getFirefightersFromMyFiredepartment()
    }

    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    )
    {
        if (firefightersFromMyFiredepartment.isEmpty()) {
            Text(
                "There aren't any firefighters in your VFD",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            firefightersFromMyFiredepartment.forEach { firefighter ->
                AppCard(
                    "👤 ${firefighter.firstName} ${firefighter.lastName}",
                    "🚒 Firedepartment: ${firefighter.firedepartmentName}",
                    "📧 Email address: ${firefighter.emailAddress} \n \uD83E\uDDD1\u200D\uD83D\uDE92 Role: ${firefighter.role}",
                    null
                )
            }
        }
    }
}