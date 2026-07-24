package com.playit.app.data.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlin.math.sqrt

/**
 * Utility for sampling PCM RMS audio amplitude from the microphone.
 * Emits float values between 0.0f and 1.0f for amplitude-reactive UI visualizers.
 *
 * Includes automatic fallback gracefully handling hardware audio recording conflicts.
 */
class AudioCapture {

    @SuppressLint("MissingPermission")
    fun sampleAmplitudeFlow(): Flow<Float> = flow {
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        var audioRecord: AudioRecord? = null
        var useHardwareMic = false

        if (minBufferSize != AudioRecord.ERROR && minBufferSize != AudioRecord.ERROR_BAD_VALUE) {
            try {
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    minBufferSize * 2
                )
                if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
                    audioRecord.startRecording()
                    useHardwareMic = true
                }
            } catch (e: Exception) {
                useHardwareMic = false
            }
        }

        try {
            val buffer = ShortArray(512)
            var phase = 0f

            while (currentCoroutineContext().isActive) {
                if (useHardwareMic && audioRecord != null) {
                    val readSize = audioRecord.read(buffer, 0, buffer.size)
                    if (readSize > 0) {
                        var sum = 0.0
                        for (i in 0 until readSize) {
                            val sample = buffer[i]
                            sum += sample * sample
                        }
                        val rms = sqrt(sum / readSize)
                        // Scale RMS: ~100 to 5000 range mapped to 0.0f..1.0f
                        val normalized = (rms / 3500.0).toFloat().coerceIn(0.05f, 1.0f)
                        emit(normalized)
                    } else {
                        // Software simulated pulsing wave fallback if read fails
                        phase += 0.2f
                        val simulated = (kotlin.math.sin(phase) * 0.35f + 0.45f).coerceIn(0.1f, 0.9f)
                        emit(simulated)
                    }
                } else {
                    // Fallback amplitude generator for visual testing or audio conflicts
                    phase += 0.25f
                    val simulated = (kotlin.math.sin(phase) * 0.35f + 0.45f).coerceIn(0.1f, 0.9f)
                    emit(simulated)
                }
                kotlinx.coroutines.delay(40) // ~25 updates per second
            }
        } finally {
            try {
                if (audioRecord != null && audioRecord.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecord.stop()
                }
                audioRecord?.release()
            } catch (e: Exception) {
                // Ignore cleanup exceptions
            }
        }
    }.flowOn(Dispatchers.IO)
}
