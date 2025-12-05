package dev.cankolay.wakeup.presentation.ui.composition

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("NavController not provided")
}

