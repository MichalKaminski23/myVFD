package com.vfd.client.ui.components.globals

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vfd.client.ui.components.elements.AppColumn

@Composable
fun AppLoadingBar(isLoading: Boolean) {
    AppColumn {
        if (isLoading) {
            LinearProgressIndicator(
                Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}