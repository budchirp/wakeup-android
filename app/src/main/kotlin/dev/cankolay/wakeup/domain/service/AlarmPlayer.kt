package dev.cankolay.wakeup.domain.service

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var player: MediaPlayer? = null

    private fun getAlarmUri() =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun play() {
        stop()

        player = MediaPlayer.create(context, getAlarmUri())
        player?.apply {
            isLooping = true

            start()
        }
    }

    fun stop() {
        player?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        
        player = null
    }
}