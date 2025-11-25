package dev.cankolay.wakeup.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.cankolay.wakeup.data.di.SettingsDataStore
import dev.cankolay.wakeup.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @SettingsDataStore private val store: DataStore<Preferences>
) : SettingsRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val KEY_URL = stringPreferencesKey("url")

    override val url: StateFlow<String?> =
        store.data
            .map { prefs -> prefs[KEY_URL] }
            .stateIn(
                scope,
                started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
                initialValue = null
            )

    override suspend fun setUrl(url: String) {
        store.edit { prefs ->
            prefs[KEY_URL] = url.trim()
        }
    }
}

