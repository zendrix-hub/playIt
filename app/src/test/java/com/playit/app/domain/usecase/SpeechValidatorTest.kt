package com.playit.app.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechValidatorTest {

    private val speechValidator = SpeechValidator()

    private val pronunciationMap = mapOf(
        "m" to listOf("m", "em", "um", "am"),
        "s" to listOf("s", "es", "suh", "is"),
        "a" to listOf("a", "ah", "uh", "apple")
    )

    @Test
    fun testSpeechValidatorMatchHighConfidence() {
        // Target matches recognized sound with high confidence (>=75%)
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "m",
            confidence = 0.85,
            pronunciationMap = pronunciationMap
        )
        assertTrue(result)
    }

    @Test
    fun testSpeechValidatorMatchLowConfidence() {
        // Target matches recognized sound but confidence is too low (<75%)
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "m",
            confidence = 0.70,
            pronunciationMap = pronunciationMap
        )
        assertFalse(result)
    }

    @Test
    fun testSpeechValidatorNoMatchHighConfidence() {
        // Target does not match recognized sound despite high confidence
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "s",
            confidence = 0.95,
            pronunciationMap = pronunciationMap
        )
        assertFalse(result)
    }

    @Test
    fun testSpeechValidatorPhoneticMatch() {
        // Recognized sound matches child-friendly phonetic variants of target sound
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "em",
            confidence = 0.80,
            pronunciationMap = pronunciationMap
        )
        assertTrue(result)
    }

    @Test
    fun testSpeechValidatorEmptyResult() {
        val result = speechValidator.validatePronunciation(
            targetSound = "m",
            recognizedText = "",
            confidence = 0.99,
            pronunciationMap = pronunciationMap
        )
        assertFalse(result)
    }
}
