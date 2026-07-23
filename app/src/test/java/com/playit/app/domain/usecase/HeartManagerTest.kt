package com.playit.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HeartManagerTest {

    private val heartManager = HeartManager(maxHearts = 3)

    @Test
    fun testProcessIncorrectAnswerDecrementsHeart() {
        val (newHearts, isDepleted) = heartManager.processIncorrectAnswer(currentHearts = 3)
        assertEquals(2, newHearts)
        assertFalse(isDepleted)
    }

    @Test
    fun testProcessIncorrectAnswerDepletionResetsToThree() {
        val (newHearts, isDepleted) = heartManager.processIncorrectAnswer(currentHearts = 1)
        assertEquals(3, newHearts)
        assertTrue(isDepleted)
    }

    @Test
    fun testProcessCorrectAnswerBelowThresholdNoBonus() {
        val hearts = heartManager.processCorrectAnswer(currentHearts = 2, consecutiveCorrect = 2)
        assertEquals(2, hearts)
    }

    @Test
    fun testProcessCorrectAnswerAtThresholdGrantsBonusClampedToMax() {
        val hearts = heartManager.processCorrectAnswer(currentHearts = 2, consecutiveCorrect = 3)
        assertEquals(3, hearts)

        val clampedHearts = heartManager.processCorrectAnswer(currentHearts = 3, consecutiveCorrect = 3)
        assertEquals(3, clampedHearts)
    }
}
