package com.playit.app.data.audio

import android.content.Context
import android.media.MediaPlayer

class PhonemeAudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun playAssetAudio(fileName: String, onComplete: () -> Unit = {}) {
        try {
            release() // Stop anything currently playing
            mediaPlayer = MediaPlayer()

            // Open the file from the assets folder
            val descriptor = context.assets.openFd(fileName)

            // Give it to the media player
            mediaPlayer?.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length
            )

            // THE FIX: Explicitly close the descriptor so it doesn't leak memory!
            descriptor.close()

            // Trigger the callback when the audio finishes playing
            mediaPlayer?.setOnCompletionListener {
                onComplete()
            }

            mediaPlayer?.prepare()
            mediaPlayer?.start()

        } catch (e: Exception) {
            e.printStackTrace()
            onComplete() // Failsafe: if the audio fails to load, let the user proceed anyway
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