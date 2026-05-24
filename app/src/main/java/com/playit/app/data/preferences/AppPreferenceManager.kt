package com.playit.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Thin wrapper around [SharedPreferences] that surfaces the two runtime
 * values the [MapScreen] stats bar needs: active hearts and current daily
 * streak. All writes are committed asynchronously via [apply].
 *
 * Construct once per process (e.g., inside [Application.onCreate]) and
 * inject into every ViewModel that needs preference access.
 */
class AppPreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ── Readers ──────────────────────────────────────────────────────────────

    /**
     * Current heart count (0–[MAX_HEARTS]). Hearts are spent on wrong
     * answers and replenish over time. Defaults to [MAX_HEARTS] on a
     * fresh install.
     */
    val activeHearts: Int
        get() = prefs.getInt(KEY_HEARTS, MAX_HEARTS).coerceIn(0, MAX_HEARTS)

    /**
     * Number of consecutive calendar days the learner has opened the app
     * and completed at least one lesson. Defaults to 0.
     */
    val currentStreak: Int
        get() = prefs.getInt(KEY_STREAK, 0).coerceAtLeast(0)

    // ── Writers ──────────────────────────────────────────────────────────────

    /**
     * Persists a new heart count, clamped to the valid range [0, [MAX_HEARTS]].
     *
     * @param count The new heart value to store.
     */
    fun setHearts(count: Int) {
        prefs.edit { putInt(KEY_HEARTS, count.coerceIn(0, MAX_HEARTS)) }
    }

    /**
     * Persists a new streak day count. Negative values are stored as 0.
     *
     * @param days The new streak length to store.
     */
    fun setStreak(days: Int) {
        prefs.edit { putInt(KEY_STREAK, days.coerceAtLeast(0)) }
    }

    /**
     * Decrements the heart count by one, flooring at 0. Safe to call when
     * the learner gives a wrong answer.
     */
    fun spendHeart() {
        setHearts(activeHearts - 1)
    }

    /**
     * Resets both hearts and streak to their default values. Intended for
     * "Reset Progress" settings flows or instrumented test setup.
     */
    fun resetAll() {
        prefs.edit {
            putInt(KEY_HEARTS, MAX_HEARTS)
            putInt(KEY_STREAK, 0)
        }
    }

    companion object {
        private const val PREFS_NAME = "playit_app_prefs"
        private const val KEY_HEARTS = "active_hearts"
        private const val KEY_STREAK = "current_streak"

        /** Maximum number of hearts a learner can hold. */
        const val MAX_HEARTS = 5
    }
}