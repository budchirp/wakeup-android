package dev.cankolay.wakeup.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.cankolay.wakeup.data.di.SettingsDataStore
import dev.cankolay.wakeup.domain.model.VibrationIntensity
import dev.cankolay.wakeup.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
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

    private val KEY_IS_SETUP_DONE = booleanPreferencesKey("is_setup_done")
    private val KEY_URL = stringPreferencesKey("url")
    private val KEY_VOLUME = floatPreferencesKey("volume")
    private val KEY_VIBRATION_INTENSITY = stringPreferencesKey("vibration_intensity")
    private val KEY_ALARM_DURATION = intPreferencesKey("alarm_duration")

    override val isSetupDone =
        store.data
            .map { prefs -> prefs[KEY_IS_SETUP_DONE] ?: false }
            .stateIn(
                scope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    override val url: StateFlow<String?> =
        store.data
            .map { prefs -> prefs[KEY_URL] }
            .stateIn(
                scope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    override val volume =
        store.data
            .map { prefs -> prefs[KEY_VOLUME] ?: 1.0f }
            .stateIn(
                scope,
                started = SharingStarted.Eagerly,
                initialValue = 1.0f
            )

    override val vibrationIntensity =
        store.data
            .map { prefs ->
                prefs[KEY_VIBRATION_INTENSITY]?.let { name ->
                    VibrationIntensity.entries.find { it.name == name }
                } ?: VibrationIntensity.MEDIUM
            }
            .stateIn(
                scope,
                started = SharingStarted.Eagerly,
                initialValue = VibrationIntensity.MEDIUM
            )

    override val alarmDuration: StateFlow<Int> =
        store.data
            .map { prefs -> prefs[KEY_ALARM_DURATION] ?: -1 }
            .stateIn(
                scope,
                started = SharingStarted.Eagerly,
                initialValue = -1
            )

    override suspend fun setSetupDone(value: Boolean) {
        store.edit { prefs ->
            prefs[KEY_IS_SETUP_DONE] = value
        }
    }

    override suspend fun setUrl(url: String) {
        store.edit { prefs ->
            prefs[KEY_URL] = url.trim()
        }
    }

    override suspend fun setVolume(volume: Float) {
        store.edit { prefs ->
            prefs[KEY_VOLUME] = volume.coerceIn(0f, 1f)
        }
    }

    override suspend fun setVibrationIntensity(intensity: VibrationIntensity) {
        store.edit { prefs ->
            prefs[KEY_VIBRATION_INTENSITY] = intensity.name
        }
    }

    override suspend fun setAlarmDuration(seconds: Int) {
        store.edit { prefs ->
            prefs[KEY_ALARM_DURATION] = if (seconds < 3) -1 else seconds.coerceIn(3, 60)
        }
    }
}
