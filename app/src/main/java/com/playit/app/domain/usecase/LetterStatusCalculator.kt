package com.playit.app.domain.usecase

/**
 * Letter mastery status enum for Parent Dashboard reporting.
 */
enum class LetterMasteryStatus {
    MASTERED,   // Green: >= 80% accuracy and < 3 failed attempts
    DEVELOPING, // Yellow: 50% - 79% accuracy and < 3 failed attempts
    AT_RISK     // Red: < 50% accuracy OR >= 3 failed attempts
}

/**
 * Domain usecase to evaluate letter status thresholds for student progress reporting.
 */
class LetterStatusCalculator {

    fun calculateStatus(
        accuracyPercentage: Int,
        failedAttempts: Int
    ): LetterMasteryStatus {
        if (failedAttempts >= 3 || accuracyPercentage < 50) {
            return LetterMasteryStatus.AT_RISK
        }
        return if (accuracyPercentage >= 80) {
            LetterMasteryStatus.MASTERED
        } else {
            LetterMasteryStatus.DEVELOPING
        }
    }
}
