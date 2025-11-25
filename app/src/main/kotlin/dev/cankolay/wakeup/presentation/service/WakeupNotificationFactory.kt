package dev.cankolay.wakeup.presentation.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cankolay.wakeup.R
import dev.cankolay.wakeup.domain.model.ConnectionState
import dev.cankolay.wakeup.presentation.AppActivity
import javax.inject.Inject

class WakeupNotificationFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun build(
        channel: String,
        status: ConnectionState,
        ringing: Boolean,
        hasUrl: Boolean
    ): Notification {
        val builder = NotificationCompat.Builder(context, channel)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(content(status, ringing))
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setContentIntent(activityIntent())

        when {
            ringing -> builder.addAction(stopAction())
            status is ConnectionState.Connected -> builder.addAction(disconnectAction())
            hasUrl -> {
                builder.addAction(connectAction())
                builder.addAction(closeAction())
            }

            else -> builder.addAction(closeAction())
        }

        return builder.build()
    }

    private fun content(status: ConnectionState, ringing: Boolean): String {
        val connection = when (status) {
            is ConnectionState.Connected -> "Connected"
            is ConnectionState.Connecting -> "Connecting..."
            is ConnectionState.Disconnected -> "Disconnected"
            is ConnectionState.Error -> "Error"
        }

        return if (ringing) {
            "Alarm is ringing!"
        } else {
            "Status: $connection"
        }
    }


    private fun connectAction() =
        NotificationCompat.Action(
            android.R.drawable.ic_menu_set_as,
            "Connect",
            serviceIntent(ServiceActions.ACTION_CONNECT, ServiceRequestCodes.CONNECT)
        )

    private fun disconnectAction() =
        NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Disconnect",
            serviceIntent(ServiceActions.ACTION_DISCONNECT, ServiceRequestCodes.DISCONNECT)
        )

    private fun stopAction() =
        NotificationCompat.Action(
            android.R.drawable.ic_media_pause,
            "Stop Alarm",
            serviceIntent(ServiceActions.ACTION_STOP_ALARM, ServiceRequestCodes.STOP_ALARM)
        )

    private fun closeAction() =
        NotificationCompat.Action(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Close",
            serviceIntent(ServiceActions.ACTION_CLOSE, ServiceRequestCodes.CLOSE)
        )

    private fun serviceIntent(action: String, requestCode: Int): PendingIntent =
        PendingIntent.getService(
            context,
            requestCode,
            Intent(context, WakeupForegroundService::class.java).apply { this.action = action },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun activityIntent() =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, AppActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
}

