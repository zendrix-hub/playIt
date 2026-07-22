package com.playit.app.data.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Utility to sample ambient audio levels from the microphone.
 * Runs in a worker thread and safely releases resources after a 300ms sample window.
 */
class AmbientNoiseMonitor {

    @SuppressLint("MissingPermission")
    suspend fun measureNoiseDb(): Double = withContext(Dispatchers.IO) {
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        if (minBufferSize == AudioRecord.ERROR || minBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            return@withContext 0.0
        }

        var audioRecord: AudioRecord? = null
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                minBufferSize * 2
            )

            if (audioRecord.state != AudioRecord.STATE_INITIALIZED) {
                return@withContext 0.0
            }

            audioRecord.startRecording()

            val buffer = ShortArray(sampleRate / 10) // 100ms buffer
            var totalRmsSum = 0.0
            var count = 0

            // Sample for 300ms (3 chunks)
            for (i in 0 until 3) {
                val readSize = audioRecord.read(buffer, 0, buffer.size)
                if (readSize > 0) {
                    var sum = 0.0
                    for (j in 0 until readSize) {
                        sum += buffer[j] * buffer[j]
                    }
                    val rms = sqrt(sum / readSize)
                    totalRmsSum += rms
                    count++
                }
                Thread.sleep(100)
            }

            audioRecord.stop()

            if (count == 0) return@withContext 0.0
            val avgRms = totalRmsSum / count
            if (avgRms <= 0.0) return@withContext 0.0

            // Mapping: 10.0 RMS = 20dB, 100.0 RMS = 40dB, 1000.0 RMS = 60dB
            return@withContext 20 * log10(avgRms)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext 0.0
        } finally {
            try {
                audioRecord?.release()
            } catch (e: Exception) {
                // Ignore safe cleanups
            }
        }
    }
}
