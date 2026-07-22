package com.playit.app.domain.usecase

/**
 * Domain use case to evaluate letter unlock progression.
 * Letter N+1 is unlocked if Letter N is completed.
 * The first letter of the sequence is unlocked by default.
 */
class UnlockManager {

    fun isLetterUnlocked(
        label: String,
        sequence: List<String>,
        completedPhonemes: Set<String>
    ): Boolean {
        val index = sequence.indexOf(label)
        if (index == -1) return false
        if (index == 0) return true // First node in sequence is always unlocked

        // Identify the previous actual letter in the sequence, skipping any BLEND checkpoints
        val prevLabel = sequence[index - 1]
        val prevLetterIdx = if (prevLabel.startsWith("BLEND_")) {
            index - 2
        } else {
            index - 1
        }

        if (prevLetterIdx < 0) return true

        val prevLetterLabel = sequence[prevLetterIdx].lowercase()
        return completedPhonemes.contains(prevLetterLabel)
    }
}
