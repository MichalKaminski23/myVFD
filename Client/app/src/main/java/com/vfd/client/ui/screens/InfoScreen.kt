package com.vfd.client.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vfd.client.ui.components.AppColumn
import com.vfd.client.ui.components.AppHorizontalDivider


@Composable
fun InfoScreen(navController: NavController) {
    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "About application",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This app was created to support the operations of Volunteer Fire Departments. " +
                    "You can view your Volunteer Fire Department resources, members (and their research/training), create events, and monitor your rescue and firefighting operations. " +
                    "There's also a voting system for purchasing new equipment for your Volunteer Fire Department.",
            style = MaterialTheme.typography.bodyMedium
        )

        AppHorizontalDivider()

        Text(
            text = "About author",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Michal Kaminski – student of the Silesian University of Technology at the Faculty of Automatic Control, Electronics and Computer Science " +
                    "- majoring in Computer Science. This app is the engineering project.",
            style = MaterialTheme.typography.bodyMedium
        )

        AppHorizontalDivider()

        Text(
            text = "Contact the author/admin",
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 27.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "admin@vfd_name@example.pl",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}