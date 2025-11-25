package dev.cankolay.wakeup.presentation.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.cankolay.wakeup.presentation.ui.composable.screen.AlarmScreen
import dev.cankolay.wakeup.presentation.ui.composable.screen.SetupScreen
import dev.cankolay.wakeup.presentation.ui.composable.screen.WaitingScreen
import dev.cankolay.wakeup.presentation.ui.viewmodel.WakeupViewModel

@Composable
fun WakeupView(
    viewModel: WakeupViewModel = hiltViewModel()
) {
    val status by viewModel.status.collectAsState()
    val ringing by viewModel.ringing.collectAsState()
    val url by viewModel.url.collectAsState()

    when {
        url == null -> {
            SetupScreen(
                onConnect = { input -> viewModel.saveUrl(value = input) }
            )
        }

        ringing -> {
            AlarmScreen(
                onStopAlarm = { viewModel.stop() },
            )
        }

        else -> {
            WaitingScreen(
                state = status,
                onDisconnect = { viewModel.disconnect() },
                onConnect = { viewModel.connect() },
            )
        }
    }
}