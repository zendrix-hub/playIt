package com.playit.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechValidatorTest {

    private val speechValidator = SpeechValidator()

    @Test
    fun testSpeechValidatorMatchHighConfidence() {
        // Target matches recognized sound with high confidence (>=75%)
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "m",
            confidence = 0.85
        )
        assertTrue(result)
    }

    @Test
    fun testSpeechValidatorMatchLowConfidence() {
        // Target matches recognized sound but confidence is too low (<75%)
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "m",
            confidence = 0.70
        )
        assertFalse(result)
    }

    @Test
    fun testSpeechValidatorNoMatchHighConfidence() {
        // Target does not match recognized sound despite high confidence
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "s",
            confidence = 0.95
        )
        assertFalse(result)
    }

    @Test
    fun testSpeechValidatorPhoneticMatch() {
        // Recognized sound matches child-friendly phonetic variants of target sound
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "em",
            confidence = 0.80
        )
        assertTrue(result)
    }

    @Test
    fun testSpeechValidatorEmptyResult() {
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "",
            confidence = 0.99
        )
        assertFalse(result)
    }

    @Test
    fun testAll28PhonemesHaveValidFallbackVariants() {
        val expected28Phonemes = listOf(
            "m", "s", "a", "i",
            "o", "b", "e", "u",
            "t", "k", "l", "y",
            "n", "g", "ng", "p",
            "r", "d", "h", "w",
            "c", "f", "j", "ñ",
            "q", "v", "x", "z"
        )

        assertEquals(28, expected28Phonemes.size)
        assertEquals(28, PhonemePronunciationMap.map.size)

        for (phoneme in expected28Phonemes) {
            val variants = PhonemePronunciationMap.map[phoneme]
            assertTrue("Phoneme '$phoneme' should be present in map", variants != null)
            assertTrue("Phoneme '$phoneme' should have at least 2 variants", (variants?.size ?: 0) >= 2)

            // Verify SpeechValidator accepts the variants
            for (variant in variants!!) {
                val isValid = speechValidator.validatePronunciation(
                    targetSound = phoneme,
                    recognizedText = variant,
                    confidence = 0.80
                )
                assertTrue("Target '$phoneme' should match variant '$variant'", isValid)
            }
        }
    }
}
