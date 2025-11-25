package dev.cankolay.wakeup.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class NotificationService : Service() {

    protected abstract val channel: String
    protected abstract val name: String
    protected abstract val id: Int

    protected open val importance: Int = NotificationManager.IMPORTANCE_LOW

    protected val serviceScope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)
    protected val mainScope = CoroutineScope(context = SupervisorJob() + Dispatchers.Main)

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        create()
    }

    override fun onDestroy() {
        super.onDestroy()

        mainScope.cancel()
        serviceScope.cancel()
    }

    protected fun start(notification: Notification) {
        startForeground(id, notification)
    }

    protected fun update(notification: Notification) {
        notificationManager.notify(id, notification)
    }

    private fun create() {
        val channel = NotificationChannel(channel, name, importance)
        notificationManager.createNotificationChannel(channel)
    }
}

