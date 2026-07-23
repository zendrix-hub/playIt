package com.playit.app.domain.usecase

import com.playit.app.domain.model.LetterCard
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpellingEngineTest {

    private val spellingEngine = SpellingEngine()

    @Test
    fun testScrambleWordPreservesAllLetters() {
        val word = "bao"
        val scrambled = spellingEngine.scrambleWord(word)

        assertEquals(3, scrambled.size)
        val scrambledWord = scrambled.map { it.char }.joinToString("").lowercase()
        assertEquals(word.split("").sorted(), scrambledWord.split("").sorted())
    }

    @Test
    fun testValidateSpellingCorrectOrder() {
        val target = "aso"
        val spelled = listOf(
            LetterCard(0, "a"),
            LetterCard(1, "s"),
            LetterCard(2, "o")
        )

        assertTrue(spellingEngine.validateSpelling(spelled, target))
    }

    @Test
    fun testValidateSpellingIncorrectOrder() {
        val target = "aso"
        val spelled = listOf(
            LetterCard(0, "s"),
            LetterCard(1, "a"),
            LetterCard(2, "o")
        )

        assertFalse(spellingEngine.validateSpelling(spelled, target))
    }

    @Test
    fun testValidateSpellingMismatchedLength() {
        val target = "aso"
        val spelled = listOf(
            LetterCard(0, "a"),
            LetterCard(1, "s")
        )

        assertFalse(spellingEngine.validateSpelling(spelled, target))
    }
}
