package dev.cankolay.wakeup.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.cankolay.wakeup.presentation.ui.composition.LocalNavController
import dev.cankolay.wakeup.presentation.ui.view.HomeView
import dev.cankolay.wakeup.presentation.ui.view.SettingsView
import dev.cankolay.wakeup.presentation.ui.view.SetupView
import dev.cankolay.wakeup.presentation.ui.viewmodel.WakeupViewModel

@Composable
fun AppNavHost(
    viewModel: WakeupViewModel = hiltViewModel()
) {
    val isSetupDone by viewModel.isSetupDone.collectAsState()
    val ringing by viewModel.ringing.collectAsState()

    val navController = rememberNavController()

    LaunchedEffect(key1 = ringing) {
        if (ringing) {
            navController.navigate(route = Route.Home) {
                popUpTo(route = Route.Home) { inclusive = true }
            }
        }
    }

    CompositionLocalProvider(value = LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = if (isSetupDone == true) Route.Home else Route.Setup
        ) {
            composable<Route.Setup> {
                SetupView()
            }

            composable<Route.Home> {
                HomeView()
            }

            composable<Route.Settings> {
                SettingsView()
            }
        }
    }
}
