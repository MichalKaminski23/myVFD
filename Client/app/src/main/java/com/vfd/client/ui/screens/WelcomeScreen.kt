package com.vfd.client.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.vfd.client.R
import com.vfd.client.ui.components.buttons.AppButton
import com.vfd.client.ui.components.elements.AppColumn
import com.vfd.client.ui.components.texts.AppText

fun setAppLanguage(tag: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
}

@Composable
fun WelcomeScreen(navController: NavController) {
    AppColumn(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    )
    {
        AppText(
            stringResource(id = R.string.app_name_long),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(40.dp))
        AppText(
            stringResource(id = R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(40.dp))
        AppButton(
            icon = Icons.Filled.Lock,
            label = stringResource(id = R.string.login),
            onClick = { navController.navigate("loginScreen") },
            fullWidth = true,
        )

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            icon = Icons.Filled.Add,
            label = stringResource(id = R.string.register),
            onClick = { navController.navigate("registerScreen") },
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            icon = Icons.Filled.Info,
            label = stringResource(id = R.string.information),
            onClick = { navController.navigate("infoScreen") },
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(32.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AppButton(
                icon = Icons.Filled.LocationOn,
                label = "PL",
                onClick = { setAppLanguage("pl") },
                modifier = Modifier.weight(1f),
            )
            AppButton(
                icon = Icons.Filled.LocationOn,
                label = "EN",
                onClick = { setAppLanguage("en") },
                modifier = Modifier.weight(1f),
            )
        }
    }
}