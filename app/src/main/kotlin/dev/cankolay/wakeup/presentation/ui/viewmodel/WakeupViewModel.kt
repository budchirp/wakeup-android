package dev.cankolay.wakeup.presentation.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cankolay.wakeup.domain.model.VibrationIntensity
import dev.cankolay.wakeup.domain.service.WakeupService
import dev.cankolay.wakeup.presentation.service.ServiceActions
import dev.cankolay.wakeup.presentation.service.WakeupForegroundService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WakeupViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wakeupService: WakeupService,
) : ViewModel() {

    val status = wakeupService.status
    val ringing = wakeupService.ringing
    val testing = wakeupService.testing

    val isSetupDone = wakeupService.isSetupDone
    val url = wakeupService.url
    val volume = wakeupService.volume
    val vibrationIntensity = wakeupService.intensity
    val alarmDuration = wakeupService.duration

    fun completeSetup(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return

        viewModelScope.launch {
            wakeupService.setUrl(trimmed)
            wakeupService.setSetupDone(true)
        }

        connect()
    }

    fun updateUrl(value: String) {
        val trimmed = value.trim()
        if (trimmed.isBlank()) return

        viewModelScope.launch {
            wakeupService.setUrl(trimmed)
        }
    }

    fun updateVolume(value: Float) {
        viewModelScope.launch {
            wakeupService.setVolume(value)
        }
    }

    fun updateVibrationIntensity(intensity: VibrationIntensity) {
        viewModelScope.launch {
            wakeupService.setVibrationIntensity(intensity)
        }
    }

    fun updateAlarmDuration(seconds: Int) {
        viewModelScope.launch {
            wakeupService.setAlarmDuration(seconds)
        }
    }

    fun test() {
        wakeupService.test()
    }

    fun disconnect() {
        context.startForegroundService(intent(action = ServiceActions.ACTION_DISCONNECT))
    }

    fun connect() {
        context.startForegroundService(intent(action = ServiceActions.ACTION_CONNECT))
    }

    fun stop() {
        context.startForegroundService(intent(action = ServiceActions.ACTION_STOP_ALARM))

        viewModelScope.launch {
            wakeupService.stop()
        }
    }

    private fun intent(action: String): Intent {
        return Intent(context, WakeupForegroundService::class.java).apply {
            this.action = action
        }
    }
}
