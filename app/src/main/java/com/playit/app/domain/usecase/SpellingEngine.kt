package com.playit.app.domain.usecase

import com.playit.app.domain.model.LetterCard

/**
 * Pure domain class to handle word scrambling and spelling correctness validation.
 */
class SpellingEngine {

    fun scrambleWord(word: String): List<LetterCard> {
        val chars = word.mapIndexed { idx, c -> LetterCard(idx, c.toString()) }
        if (chars.size <= 1) return chars
        var scrambled = chars.shuffled()
        var attempts = 0
        while (scrambled.map { it.char }.joinToString("").lowercase() == word.lowercase() && attempts < 10) {
            scrambled = chars.shuffled()
            attempts++
        }
        return scrambled
    }

    fun validateSpelling(spelled: List<LetterCard>, target: String): Boolean {
        if (spelled.size != target.length) return false
        val spelledString = spelled.map { it.char }.joinToString("").lowercase()
        return spelledString == target.lowercase()
    }
}
