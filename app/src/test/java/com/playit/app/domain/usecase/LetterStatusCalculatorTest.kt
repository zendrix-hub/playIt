package com.playit.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class LetterStatusCalculatorTest {

    private val calculator = LetterStatusCalculator()

    @Test
    fun testMasteredStatusForHighAccuracyAndLowFailures() {
        val status = calculator.calculateStatus(accuracyPercentage = 85, failedAttempts = 0)
        assertEquals(LetterMasteryStatus.MASTERED, status)
    }

    @Test
    fun testMasteredStatusExactly80Percent() {
        val status = calculator.calculateStatus(accuracyPercentage = 80, failedAttempts = 2)
        assertEquals(LetterMasteryStatus.MASTERED, status)
    }

    @Test
    fun testDevelopingStatusBetween50And79Percent() {
        val status = calculator.calculateStatus(accuracyPercentage = 75, failedAttempts = 1)
        assertEquals(LetterMasteryStatus.DEVELOPING, status)
    }

    @Test
    fun testAtRiskStatusWhenAccuracyBelow50Percent() {
        val status = calculator.calculateStatus(accuracyPercentage = 45, failedAttempts = 0)
        assertEquals(LetterMasteryStatus.AT_RISK, status)
    }

    @Test
    fun testAtRiskStatusWhenFailedAttemptsThreeOrMore() {
        // High accuracy but 3 or more failed attempts triggers AT_RISK status
        val status = calculator.calculateStatus(accuracyPercentage = 90, failedAttempts = 3)
        assertEquals(LetterMasteryStatus.AT_RISK, status)
    }
}
