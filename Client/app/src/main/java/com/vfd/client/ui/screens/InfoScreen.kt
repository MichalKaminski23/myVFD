package com.vfd.client.ui.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vfd.client.R
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.elements.AppHorizontalDivider
import com.vfd.client.ui.components.texts.AppText


@Composable
fun InfoScreen() {
    AppColumn(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    )
    {
        AppText(
            text = stringResource(id = R.string.about_app),
            style = MaterialTheme.typography.headlineLarge
        )
        AppText(
            text = stringResource(id = R.string.about_app_description),
            style = MaterialTheme.typography.bodyMedium
        )
        AppHorizontalDivider()

        AppText(
            stringResource(id = R.string.about_author),
            style = MaterialTheme.typography.headlineLarge
        )
        AppText(
            text = stringResource(id = R.string.about_author_description),
            style = MaterialTheme.typography.bodyMedium
        )
        AppHorizontalDivider()

        AppText(
            stringResource(id = R.string.contact),
            style = MaterialTheme.typography.headlineLarge
        )
        AppText("adminMyVFD@example.pl", style = MaterialTheme.typography.bodyMedium)
    }
}