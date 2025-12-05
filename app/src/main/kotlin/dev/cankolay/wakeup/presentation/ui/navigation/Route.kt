package dev.cankolay.wakeup.presentation.ui.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Setup : Route()

    @Serializable
    data object Home : Route()

    @Serializable
    data object Settings : Route()
}
