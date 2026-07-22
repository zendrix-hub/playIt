package com.playit.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class StatsTrackersTest {

    private val starCalculator = StarCalculator()
    private val streakTracker = StreakTracker()

    // ─── StarCalculator Tests ────────────────────────────────────────────────

    @Test
    fun testStarCalculatorSum() {
        val stars = listOf(3, 2, 1, 3)
        assertEquals(9, starCalculator.calculateTotalStars(stars))
    }

    @Test
    fun testStarCalculatorEmpty() {
        val stars = emptyList<Int>()
        assertEquals(0, starCalculator.calculateTotalStars(stars))
    }

    // ─── StreakTracker Tests ─────────────────────────────────────────────────

    @Test
    fun testFirstPlaySetsStreakTo1() {
        val current = System.currentTimeMillis()
        val (streak, timestamp) = streakTracker.updateStreak(
            currentStreak = 0,
            lastPlayedTimestamp = null,
            currentTimestamp = current
        )
        assertEquals(1, streak)
        assertEquals(current, timestamp)
    }

    @Test
    fun testSameDayPlayPreservesStreak() {
        val current = System.currentTimeMillis()
        // Last played was 1 hour ago (same day)
        val lastPlayed = current - 1 * 60 * 60 * 1000

        val (streak, timestamp) = streakTracker.updateStreak(
            currentStreak = 5,
            lastPlayedTimestamp = lastPlayed,
            currentTimestamp = current
        )
        assertEquals(5, streak)
        assertEquals(current, timestamp)
    }

    @Test
    fun testNextDayPlayIncrementsStreak() {
        val currentCal = Calendar.getInstance()
        val currentTimestamp = currentCal.timeInMillis

        // Last played was yesterday
        val yesterdayCal = Calendar.getInstance().apply {
            timeInMillis = currentTimestamp
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val lastPlayed = yesterdayCal.timeInMillis

        val (streak, timestamp) = streakTracker.updateStreak(
            currentStreak = 4,
            lastPlayedTimestamp = lastPlayed,
            currentTimestamp = currentTimestamp
        )
        assertEquals(5, streak)
        assertEquals(currentTimestamp, timestamp)
    }

    @Test
    fun testGapDayPlayResetsStreakTo1() {
        val currentCal = Calendar.getInstance()
        val currentTimestamp = currentCal.timeInMillis

        // Last played was 3 days ago (gap day)
        val pastCal = Calendar.getInstance().apply {
            timeInMillis = currentTimestamp
            add(Calendar.DAY_OF_YEAR, -3)
        }
        val lastPlayed = pastCal.timeInMillis

        val (streak, timestamp) = streakTracker.updateStreak(
            currentStreak = 10,
            lastPlayedTimestamp = lastPlayed,
            currentTimestamp = currentTimestamp
        )
        assertEquals(1, streak)
        assertEquals(currentTimestamp, timestamp)
    }
}
