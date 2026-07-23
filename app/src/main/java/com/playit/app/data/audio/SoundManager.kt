package com.playit.app.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes
import com.playit.app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Task UI-10.02 — Centralized SoundManager for PlayIT sound design pass.
 *
 * Manages playback for all 6 spec-mandated sound events:
 * 1. CORRECT_ANSWER   -> reward chime
 * 2. INCORRECT_ANSWER -> soft pop
 * 3. HEART_LOSS       -> gentle whoosh
 * 4. HEART_RECOVERY   -> bright sparkle
 * 5. NODE_UNLOCK       -> magical chime
 * 6. LEVEL_COMPLETE   -> celebration fanfare
 *
 * Implements overlap prevention via a serial Mutex queue per Master Context §8
 * so near-simultaneous events (e.g. correct answer immediately followed by heart recovery)
 * play distinctly without colliding or sounding garbled.
 */
class SoundManager private constructor(private val context: Context) {

    enum class SoundEvent {
        CORRECT_ANSWER,
        INCORRECT_ANSWER,
        HEART_LOSS,
        HEART_RECOVERY,
        NODE_UNLOCK,
        LEVEL_COMPLETE
    }

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mutex = Mutex()
    private var soundPool: SoundPool? = null
    private val soundIds = mutableMapOf<SoundEvent, Int>()

    init {
        initSoundPool()
    }

    private fun initSoundPool() {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(attributes)
            .build()

        loadSound(SoundEvent.CORRECT_ANSWER, R.raw.sfx_correct_reward_chime)
        loadSound(SoundEvent.INCORRECT_ANSWER, R.raw.sfx_incorrect_soft_pop)
        loadSound(SoundEvent.HEART_LOSS, R.raw.sfx_heart_loss_gentle_whoosh)
        loadSound(SoundEvent.HEART_RECOVERY, R.raw.sfx_heart_recovery_bright_sparkle)
        loadSound(SoundEvent.NODE_UNLOCK, R.raw.sfx_node_unlock_magical_chime)
        loadSound(SoundEvent.LEVEL_COMPLETE, R.raw.sfx_level_complete_fanfare)
    }

    private fun loadSound(event: SoundEvent, @RawRes resId: Int) {
        try {
            val id = soundPool?.load(context, resId, 1) ?: 0
            if (id != 0) {
                soundIds[event] = id
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Plays the specified sound event with strict overlap prevention.
     * Near-simultaneous invocations are serialized using [mutex].
     */
    fun playSound(event: SoundEvent) {
        scope.launch {
            mutex.withLock {
                val soundId = soundIds[event] ?: return@withLock
                soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
                
                // Enforce spacing based on event audio duration to prevent overlap garbling
                val spacingMs = when (event) {
                    SoundEvent.INCORRECT_ANSWER -> 200L
                    SoundEvent.HEART_LOSS -> 300L
                    SoundEvent.CORRECT_ANSWER -> 350L
                    SoundEvent.HEART_RECOVERY -> 400L
                    SoundEvent.NODE_UNLOCK -> 450L
                    SoundEvent.LEVEL_COMPLETE -> 700L
                }
                delay(spacingMs)
            }
        }
    }

    fun playCorrectAnswer() = playSound(SoundEvent.CORRECT_ANSWER)
    fun playIncorrectAnswer() = playSound(SoundEvent.INCORRECT_ANSWER)
    fun playHeartLoss() = playSound(SoundEvent.HEART_LOSS)
    fun playHeartRecovery() = playSound(SoundEvent.HEART_RECOVERY)
    fun playNodeUnlock() = playSound(SoundEvent.NODE_UNLOCK)
    fun playLevelComplete() = playSound(SoundEvent.LEVEL_COMPLETE)

    fun release() {
        soundPool?.release()
        soundPool = null
    }

    companion object {
        @Volatile
        private var INSTANCE: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SoundManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
