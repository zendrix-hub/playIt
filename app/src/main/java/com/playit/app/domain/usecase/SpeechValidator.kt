package com.playit.app.domain.usecase

/**
 * Domain usecase to validate speech recognition.
 * Matches recognized text to target sound mapping and checks confidence score.
 */
class SpeechValidator {

    fun validatePronunciation(
        targetSound: String,
        recognizedText: String,
        confidence: Double,
        pronunciationMap: Map<String, List<String>>
    ): Boolean {
        val target = targetSound.lowercase().trim()
        val heard = recognizedText.lowercase().trim()

        if (heard.isEmpty()) return false

        // Check 75% confidence score rule
        if (confidence < 0.75) return false

        val validMappings = pronunciationMap[target] ?: listOf(target)
        return validMappings.any { heard.contains(it.lowercase()) }
    }
}
