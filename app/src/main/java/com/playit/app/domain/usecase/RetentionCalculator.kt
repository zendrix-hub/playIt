package com.playit.app.domain.usecase

/**
 * Domain usecase to compute retention scores over a 7-day rolling window.
 */
class RetentionCalculator {

    /**
     * Calculates composite retention percentage (0..100) based on completed curriculum phonemes
     * and practice attempt accuracy.
     */
    fun calculateRetentionScore(
        completedPhonemesCount: Int,
        totalPhonemesCount: Int = 28,
        practiceAccuracyPercentage: Int = 100
    ): Int {
        if (totalPhonemesCount <= 0 || completedPhonemesCount <= 0) return 0
        val completionRatio = (completedPhonemesCount.toFloat() / totalPhonemesCount.toFloat()).coerceIn(0f, 1f)
        val accuracyRatio = (practiceAccuracyPercentage.toFloat() / 100f).coerceIn(0f, 1f)

        // 70% weight on completion coverage, 30% weight on practice accuracy
        val compositeScore = (completionRatio * 0.70f + accuracyRatio * 0.30f) * 100f
        return compositeScore.toInt().coerceIn(0, 100)
    }

    /**
     * Filters practice attempt timestamps within a 7-day rolling window.
     */
    fun isWithinRetentionWindow(
        attemptTimestamp: Long,
        currentTimestamp: Long,
        windowDays: Int = 7
    ): Boolean {
        if (attemptTimestamp > currentTimestamp) return false
        val windowMillis = windowDays * 24L * 60L * 60L * 1000L
        return (currentTimestamp - attemptTimestamp) <= windowMillis
    }
}
