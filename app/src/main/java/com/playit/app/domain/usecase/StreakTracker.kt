package com.playit.app.domain.usecase

import java.util.Calendar

/**
 * Domain usecase to compute streaks based on consecutive day rules.
 * Preserves streak on same-day plays, increments on next-day plays, and resets on gap days.
 */
class StreakTracker {

    fun updateStreak(
        currentStreak: Int,
        lastPlayedTimestamp: Long?,
        currentTimestamp: Long
    ): Pair<Int, Long> {
        if (lastPlayedTimestamp == null) {
            return Pair(1, currentTimestamp)
        }

        val lastPlayedCal = Calendar.getInstance().apply { timeInMillis = lastPlayedTimestamp }
        val currentCal = Calendar.getInstance().apply { timeInMillis = currentTimestamp }

        val isSameDay = lastPlayedCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                lastPlayedCal.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)

        if (isSameDay) {
            // Same day play: keep streak, update last played timestamp
            return Pair(maxOf(1, currentStreak), currentTimestamp)
        }

        // Check if last played was yesterday
        val yesterdayCal = Calendar.getInstance().apply {
            timeInMillis = currentTimestamp
            add(Calendar.DAY_OF_YEAR, -1)
        }

        val isYesterday = lastPlayedCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                lastPlayedCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)

        return if (isYesterday) {
            Pair(currentStreak + 1, currentTimestamp)
        } else {
            // Missed a day: reset streak to 1
            Pair(1, currentTimestamp)
        }
    }
}
