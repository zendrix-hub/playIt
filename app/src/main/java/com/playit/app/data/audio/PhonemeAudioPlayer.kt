package com.playit.app.data.audio

import android.content.Context
import android.media.MediaPlayer

class PhonemeAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun playAssetAudio(
        fileName: String,
        onComplete: () -> Unit = {},
        onError: ((String) -> Unit)? = null
    ) {
        try {
            // Safe reset: ensure we don't crash if it's already released or idle
            mediaPlayer?.let {
                if (it.isPlaying) it.stop()
                it.reset()
            } ?: run {
                mediaPlayer = MediaPlayer()
            }

            val descriptor = context.assets.openFd(fileName)
            mediaPlayer?.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )
            descriptor.close()

            mediaPlayer?.setOnCompletionListener {
                onComplete()
            }

            mediaPlayer?.setOnErrorListener { _, _, _ ->
                onError?.invoke("Failed to play audio asset: $fileName")
                onComplete()
                true
            }

            // Use prepareAsync for smoother performance in chains
            mediaPlayer?.setOnPreparedListener {
                it.start()
            }
            mediaPlayer?.prepareAsync()

        } catch (e: Exception) {
            e.printStackTrace()
            onError?.invoke("Audio asset '$fileName' is missing or unreadable")
            onComplete() // Failsafe
        }
    }

    fun release() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
            }
            mediaPlayer?.release()
        } catch (e: IllegalStateException) {
            // MediaPlayer was already released or in a bad state. Safely ignore.
        } finally {
            mediaPlayer = null
        }
    }
}