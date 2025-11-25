package dev.cankolay.wakeup.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.cankolay.wakeup.presentation.service.WakeupForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.type == Intent.ACTION_BOOT_COMPLETED) {
            WakeupForegroundService.start(context = context)
        }
    }
}

