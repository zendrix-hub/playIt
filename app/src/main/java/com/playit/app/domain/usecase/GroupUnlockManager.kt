package com.playit.app.domain.usecase

/**
 * Domain use case to evaluate Blend It checkpoint unlock progression.
 * A Blend It checkpoint is unlocked only if all members of its Marungko group are completed.
 */
class GroupUnlockManager {

    fun isBlendItUnlocked(
        groupLetters: List<String>,
        completedPhonemes: Set<String>
    ): Boolean {
        if (groupLetters.isEmpty()) return false
        return groupLetters.all { letter ->
            completedPhonemes.contains(letter.lowercase())
        }
    }
}
