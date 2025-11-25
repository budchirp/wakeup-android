package dev.cankolay.wakeup.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val url: StateFlow<String?>
    suspend fun setUrl(url: String)
}

