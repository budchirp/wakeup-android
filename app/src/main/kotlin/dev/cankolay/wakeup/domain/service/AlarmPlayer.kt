package dev.cankolay.wakeup.domain.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var player: MediaPlayer? = null
    private val vibrator = (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private fun getAlarmUri() =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun play() {
        stop()

        val targetVolume = (audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * 0.5f).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, targetVolume, 0)

        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )

            setDataSource(context, getAlarmUri())

            isLooping = true

            prepare()
            start()
        }

        startVibration()
    }

    fun stop() {
        player?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }
        
        player = null
        stopVibration()
    }

    private fun startVibration() {
        vibrator.vibrate(VibrationEffect.createWaveform(
            longArrayOf(0, 1000, 1000),
            0
        ))
    }

    private fun stopVibration() {
        vibrator.cancel()
    }
}