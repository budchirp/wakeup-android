package dev.cankolay.wakeup.presentation.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cankolay.wakeup.presentation.ui.composable.screen.AlarmScreen
import dev.cankolay.wakeup.presentation.ui.composable.screen.WaitingScreen
import dev.cankolay.wakeup.presentation.ui.viewmodel.WakeupViewModel

@Composable
fun HomeView(
    viewModel: WakeupViewModel = hiltViewModel()
) {
    val status by viewModel.status.collectAsState()
    val ringing by viewModel.ringing.collectAsState()

    if (ringing) {
        AlarmScreen(
            onStopAlarm = { viewModel.stop() }
        )
    } else {
        WaitingScreen(
            state = status,
            onDisconnect = { viewModel.disconnect() },
            onConnect = { viewModel.connect() }
        )
    }
}

