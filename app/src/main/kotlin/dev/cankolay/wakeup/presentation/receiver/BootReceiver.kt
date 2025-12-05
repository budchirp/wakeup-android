package dev.cankolay.wakeup.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dev.cankolay.wakeup.presentation.service.WakeupForegroundService

class BootReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                try {
                    Log.d(TAG, "Starting WakeupForegroundService on boot")
                    WakeupForegroundService.start(context = context)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start WakeupForegroundService", e)
                }
            }
        }
    }
}

