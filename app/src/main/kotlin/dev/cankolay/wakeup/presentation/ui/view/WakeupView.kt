package dev.cankolay.wakeup.presentation.ui.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dev.cankolay.wakeup.presentation.ui.navigation.AppNavHost
import dev.cankolay.wakeup.presentation.ui.viewmodel.WakeupViewModel

@Composable
fun WakeupView(
    viewModel: WakeupViewModel = hiltViewModel()
) {
    val isSetupDone by viewModel.isSetupDone.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        isSetupDone?.let {
            AppNavHost()
        }
    }
}
