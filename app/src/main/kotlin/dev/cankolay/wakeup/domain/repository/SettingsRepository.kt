package dev.cankolay.wakeup.domain.repository

import dev.cankolay.wakeup.domain.model.VibrationIntensity
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val isSetupDone: StateFlow<Boolean?>
    val url: StateFlow<String?>
    val volume: StateFlow<Float>
    val vibrationIntensity: StateFlow<VibrationIntensity>
    val alarmDuration: StateFlow<Int>

    suspend fun setSetupDone(value: Boolean)
    suspend fun setUrl(url: String)
    suspend fun setVolume(volume: Float)
    suspend fun setVibrationIntensity(intensity: VibrationIntensity)
    suspend fun setAlarmDuration(seconds: Int)
}
