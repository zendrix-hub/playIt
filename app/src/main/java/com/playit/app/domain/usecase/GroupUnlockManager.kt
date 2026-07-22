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

    /**
     * Evaluates if a target Marungko group is unlocked based on completion of all members of the preceding group.
     * Group 1 is unlocked by default.
     */
    fun isGroupUnlocked(
        groupNumber: Int,
        prevGroupLetters: List<String>,
        completedPhonemes: Set<String>
    ): Boolean {
        if (groupNumber <= 1) return true
        if (prevGroupLetters.isEmpty()) return false
        return prevGroupLetters.all { letter ->
            completedPhonemes.contains(letter.lowercase())
        }
    }
}
