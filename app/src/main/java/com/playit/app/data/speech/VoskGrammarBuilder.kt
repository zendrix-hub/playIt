package com.playit.app.data.speech

import org.json.JSONArray

object VoskGrammarBuilder {

    /**
     * Converts a list of blends into the JSON Array grammar format required by Vosk.
     * Example output: `["am", "is", "mom", "[unk]"]`
     */
    fun buildGrammar(activeBlends: List<String>): String {
        if (activeBlends.isEmpty()) {
            return "[\"[unk]\"]"
        }

        val phraseArray = JSONArray().apply {
            activeBlends.forEach { blend -> put(blend.lowercase()) }
            // ALWAYS include [unk] so Vosk can safely discard background noise
            put("[unk]")
        }

        return phraseArray.toString()
    }
}