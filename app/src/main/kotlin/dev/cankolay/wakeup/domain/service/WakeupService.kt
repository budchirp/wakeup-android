package dev.cankolay.wakeup.domain.service

import dev.cankolay.wakeup.domain.model.VibrationIntensity
import dev.cankolay.wakeup.domain.repository.SettingsRepository
import dev.cankolay.wakeup.domain.repository.WakeupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakeupService @Inject constructor(
    private val repository: WakeupRepository,
    private val settingsRepository: SettingsRepository,
    private val player: AlarmPlayer
) {
    private val scope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)

    val status = repository.status
    val ringing = MutableStateFlow(value = false)
    val testing = MutableStateFlow(value = false)

    val isSetupDone = settingsRepository.isSetupDone
    val url = settingsRepository.url
    val volume = settingsRepository.volume
    val intensity = settingsRepository.vibrationIntensity
    val duration = settingsRepository.alarmDuration

    suspend fun setSetupDone(done: Boolean) {
        settingsRepository.setSetupDone(done)
    }

    suspend fun setUrl(url: String) {
        settingsRepository.setUrl(url)
    }

    suspend fun setVolume(volume: Float) {
        settingsRepository.setVolume(volume)
    }

    suspend fun setVibrationIntensity(intensity: VibrationIntensity) {
        settingsRepository.setVibrationIntensity(intensity)
    }

    suspend fun setAlarmDuration(seconds: Int) {
        settingsRepository.setAlarmDuration(seconds)
    }

    suspend fun connect() {
        val url = settingsRepository.url.value ?: return

        repository.connect(url = url)
    }

    suspend fun disconnect() {
        repository.disconnect()

        stop()
    }

    fun stop() {
        if (!ringing.value) return

        ringing.value = false
        player.stop()
    }

    fun test() {
        if (testing.value) {
            stopTest()
            return
        }

        testing.value = true
        player.play(
            volume = volume.value,
            intensity = intensity.value,
            duration = -1,
            onEnd = { stopTest() }
        )
    }

    private fun stopTest() {
        if (!testing.value) return

        testing.value = false
        player.stop()
    }

    private fun play() {
        ringing.value = true
        player.play(
            volume = volume.value,
            intensity = intensity.value,
            duration = duration.value,
            onEnd = { stop() }
        )
    }

    init {
        scope.launch {
            repository.ringEvents.collectLatest {
                play()
            }
        }
    }
}
