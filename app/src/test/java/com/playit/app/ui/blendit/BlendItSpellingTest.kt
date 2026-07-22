package com.playit.app.ui.blendit

import com.playit.app.domain.model.LetterCard
import com.playit.app.domain.usecase.SpellingEngine
import org.junit.Assert.*
import org.junit.Test

class BlendItSpellingTest {

    private val spellingEngine = SpellingEngine()

    @Test
    fun testScrambleWordIsShuffled() {
        val word = "sam"
        val scrambled = spellingEngine.scrambleWord(word)
        assertEquals(3, scrambled.size)
        // Verify characters match
        val chars = scrambled.map { it.char }.sorted()
        assertEquals(listOf("a", "m", "s"), chars)
    }

    @Test
    fun testCorrectSpellingSucceeds() {
        val target = "am"
        val spelled = listOf(
            LetterCard(0, "a"),
            LetterCard(1, "m")
        )
        val isCorrect = spellingEngine.validateSpelling(spelled, target)
        assertTrue(isCorrect)
    }

    @Test
    fun testIncorrectSpellingFails() {
        val target = "am"
        val spelled = listOf(
            LetterCard(0, "m"),
            LetterCard(1, "a")
        )
        val isCorrect = spellingEngine.validateSpelling(spelled, target)
        assertFalse(isCorrect)
    }

    @Test
    fun testIncompleteSpellingFails() {
        val target = "sam"
        val spelled = listOf(
            LetterCard(0, "s"),
            LetterCard(1, "a")
        )
        val isCorrect = spellingEngine.validateSpelling(spelled, target)
        assertFalse(isCorrect)
    }
}
