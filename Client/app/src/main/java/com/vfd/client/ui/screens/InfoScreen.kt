package com.vfd.client.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppHorizontalDivider
import com.vfd.client.ui.components.AppText


@Composable
fun InfoScreen(navController: NavController) {
    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {

        AppText(text = "About application", style = MaterialTheme.typography.headlineLarge)
        AppText(
            text = "This app was created to support the operations of Volunteer Fire Departments. " +
                    "You can view your Volunteer Fire Department resources, members (and their research/training), create events, and monitor your rescue and firefighting operations. " +
                    "There's also a voting system for purchasing new equipment for your Volunteer Fire Department.",
            style = MaterialTheme.typography.bodyMedium
        )
        AppHorizontalDivider()

        AppText("About author", style = MaterialTheme.typography.headlineLarge)
        AppText(
            text = "Michal Kaminski – student of the Silesian University of Technology at the Faculty of Automatic Control, Electronics and Computer Science " +
                    "- majoring in Computer Science. This app is the engineering project.",
            style = MaterialTheme.typography.bodyMedium
        )
        AppHorizontalDivider()

        AppText("Contact", style = MaterialTheme.typography.headlineLarge)
        AppText("dmin@vfd_name@example.pl", style = MaterialTheme.typography.bodyMedium)
    }
}