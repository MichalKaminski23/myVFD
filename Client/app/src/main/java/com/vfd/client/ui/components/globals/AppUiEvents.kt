package com.vfd.client.ui.components.globals

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.vfd.client.utils.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun AppUiEvents(
    uiEvents: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiEvents) {
        uiEvents.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "OK",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is UiEvent.Error -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "OK",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }
}