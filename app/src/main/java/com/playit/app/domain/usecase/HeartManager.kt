package com.playit.app.domain.usecase

/**
 * Domain usecase for heart lifecycle management across learning sublevels.
 * Business rules:
 * - Default hearts max = 3 (or 5 for extended challenges).
 * - Decrements on incorrect attempt.
 * - Restores to 3 confidence-building hearts on depletion (hearts <= 0).
 * - Grants heart bonus on consecutive correct streaks, clamped to maxHearts.
 */
class HeartManager(
    val maxHearts: Int = 3
) {
    /**
     * Deducts a heart on incorrect answer. Returns Pair(newHeartCount, isDepleted).
     */
    fun processIncorrectAnswer(currentHearts: Int): Pair<Int, Boolean> {
        val nextHearts = currentHearts - 1
        return if (nextHearts <= 0) {
            Pair(3, true) // Reset to 3 confidence-building hearts
        } else {
            Pair(nextHearts, false)
        }
    }

    /**
     * Grants a heart bonus if consecutive correct count reaches threshold.
     */
    fun processCorrectAnswer(currentHearts: Int, consecutiveCorrect: Int, bonusThreshold: Int = 3): Int {
        return if (consecutiveCorrect >= bonusThreshold) {
            minOf(maxHearts, currentHearts + 1)
        } else {
            currentHearts
        }
    }
}
