package com.playit.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RetentionCalculatorTest {

    private val calculator = RetentionCalculator()

    @Test
    fun testZeroCompletedPhonemesReturnsZeroScore() {
        val score = calculator.calculateRetentionScore(completedPhonemesCount = 0, totalPhonemesCount = 28)
        assertEquals(0, score)
    }

    @Test
    fun testFullCompletionHighAccuracyReturnsHundredScore() {
        val score = calculator.calculateRetentionScore(
            completedPhonemesCount = 28,
            totalPhonemesCount = 28,
            practiceAccuracyPercentage = 100
        )
        assertEquals(100, score)
    }

    @Test
    fun testPartialCompletionWeightedScore() {
        // 14 of 28 completed (50% coverage * 0.70 = 0.35) + 100% accuracy (* 0.30 = 0.30) = 65%
        val score = calculator.calculateRetentionScore(
            completedPhonemesCount = 14,
            totalPhonemesCount = 28,
            practiceAccuracyPercentage = 100
        )
        assertEquals(65, score)
    }

    @Test
    fun testRetentionWindowWithin7Days() {
        val now = System.currentTimeMillis()
        val threeDaysAgo = now - 3L * 24 * 60 * 60 * 1000

        assertTrue(calculator.isWithinRetentionWindow(threeDaysAgo, now, windowDays = 7))
    }

    @Test
    fun testRetentionWindowOutside7Days() {
        val now = System.currentTimeMillis()
        val tenDaysAgo = now - 10L * 24 * 60 * 60 * 1000

        assertFalse(calculator.isWithinRetentionWindow(tenDaysAgo, now, windowDays = 7))
    }
}
