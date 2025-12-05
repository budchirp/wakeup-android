package dev.cankolay.wakeup.presentation.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import dev.cankolay.wakeup.domain.service.WakeupService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WakeupForegroundService : NotificationService() {

    @Inject
    lateinit var notificationFactory: WakeupNotificationFactory

    @Inject
    lateinit var wakeupService: WakeupService

    companion object {
        const val CHANNEL_ID = "WakeupForegroundServiceChannel"
        const val NOTIFICATION_ID = 1

        fun start(context: Context) {
            context.startForegroundService(Intent(context, WakeupForegroundService::class.java))
        }
    }

    override val channel = CHANNEL_ID
    override val name = "WakeUp Service"
    override val id = NOTIFICATION_ID

    override fun onCreate() {
        super.onCreate()

        serviceScope.launch {
            wakeupService.url.filterNotNull().first()
            wakeupService.connect()
        }

        observe()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handle(intent)

        start(
            notificationFactory.build(
                channel = channel,
                status = wakeupService.status.value,
                ringing = wakeupService.ringing.value,
                hasUrl = wakeupService.url.value != null
            )
        )

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()

        disconnect()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        startForegroundService(
            Intent(
                applicationContext,
                WakeupForegroundService::class.java
            ).apply {
                action = ServiceActions.ACTION_CONNECT
            })
    }

    private fun handle(intent: Intent?) {
        when (intent?.action) {
            ServiceActions.ACTION_CONNECT -> connect()

            ServiceActions.ACTION_DISCONNECT -> disconnect()

            ServiceActions.ACTION_STOP_ALARM -> {
                wakeupService.stop()

                refresh()
            }

            ServiceActions.ACTION_CLOSE -> {
                disconnect()

                stopSelf()
            }
        }
    }

    private fun connect() {
        serviceScope.launch {
            // Wait for DataStore to load URL before connecting
            wakeupService.url.filterNotNull().first()
            wakeupService.connect()
        }
    }

    private fun disconnect() {
        serviceScope.launch {
            wakeupService.disconnect()
        }
    }

    private fun observe() {
        serviceScope.launch {
            wakeupService.status.collectLatest {
                refresh()
            }
        }

        serviceScope.launch {
            wakeupService.ringing.collectLatest {
                refresh()
            }
        }

        serviceScope.launch {
            wakeupService.url.collectLatest {
                refresh()
            }
        }
    }

    private fun refresh() {
        mainScope.launch {
            update(
                notificationFactory.build(
                    channel = channel,
                    status = wakeupService.status.value,
                    ringing = wakeupService.ringing.value,
                    hasUrl = wakeupService.url.value != null
                )
            )
        }
    }
}

