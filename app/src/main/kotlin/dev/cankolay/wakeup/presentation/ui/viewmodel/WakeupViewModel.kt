package dev.cankolay.wakeup.presentation.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    val url = wakeupService.url

    fun saveUrl(value: String) {
        val trimmed = value.trim()
        if (trimmed.isBlank() || url.value != null) return

        viewModelScope.launch {
            wakeupService.setUrl(trimmed)
        }

        connect()
    }

    fun disconnect() {
        context.startForegroundService(intent(ServiceActions.ACTION_DISCONNECT))
    }

    fun connect() {
        context.startForegroundService(intent(ServiceActions.ACTION_CONNECT))
    }

    fun stop() {
        context.startForegroundService(intent(ServiceActions.ACTION_STOP_ALARM))

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

