package dev.cankolay.wakeup.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.cankolay.wakeup.presentation.service.ServiceActions
import dev.cankolay.wakeup.presentation.service.WakeupForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || 
            intent.action == Intent.ACTION_MY_PACKAGE_REPLACED ||
            intent.action == Intent.ACTION_PACKAGE_REPLACED) {
            
            context.startForegroundService(Intent(context, WakeupForegroundService::class.java).apply {
                action = ServiceActions.ACTION_CONNECT
            })
        }
    }
}

