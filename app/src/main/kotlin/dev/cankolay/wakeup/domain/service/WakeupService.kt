package dev.cankolay.wakeup.domain.service

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

    val url = settingsRepository.url

    suspend fun setUrl(url: String) {
        settingsRepository.setUrl(url)
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

    private fun play() {
        ringing.value = true
        player.play()
    }

    init {
        scope.launch {
            repository.ringEvents.collectLatest {
                play()
            }
        }
    }
}


