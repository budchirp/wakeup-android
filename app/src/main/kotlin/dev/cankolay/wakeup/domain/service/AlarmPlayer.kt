package dev.cankolay.wakeup.domain.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.cankolay.wakeup.domain.model.VibrationIntensity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var player: MediaPlayer? = null
    private var durationJob: Job? = null

    private val vibrator =
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var onAlarmEnd: (() -> Unit)? = null

    private fun getAlarmUri() =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    fun play(
        volume: Float,
        intensity: VibrationIntensity,
        duration: Int,
        onEnd: () -> Unit
    ) {
        stop()

        onAlarmEnd = onEnd

        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        val targetVolume = (maxVolume * volume).toInt().coerceIn(0, maxVolume)
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

        startVibration(intensity)

        if (duration > 0) {
            durationJob = scope.launch {
                delay(timeMillis = duration * 1000L)
                onAlarmEnd?.invoke()
            }
        }
    }

    fun stop() {
        durationJob?.cancel()
        durationJob = null

        player?.apply {
            if (isPlaying) stop()
            reset()
            release()
        }

        player = null
        onAlarmEnd = null
        stopVibration()
    }

    private fun startVibration(intensity: VibrationIntensity) {
        val pattern = when (intensity) {
            VibrationIntensity.LOW -> longArrayOf(0, 500, 1500)
            VibrationIntensity.MEDIUM -> longArrayOf(0, 800, 800)
            VibrationIntensity.HIGH -> longArrayOf(0, 1000, 500)
        }

        val amplitudes = when (intensity) {
            VibrationIntensity.LOW -> intArrayOf(0, 80, 0)
            VibrationIntensity.MEDIUM -> intArrayOf(0, 160, 0)
            VibrationIntensity.HIGH -> intArrayOf(0, 255, 0)
        }

        vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, 0))
    }

    private fun stopVibration() {
        vibrator.cancel()
    }
}
