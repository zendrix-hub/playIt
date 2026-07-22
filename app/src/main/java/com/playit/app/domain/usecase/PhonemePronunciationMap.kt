package com.playit.app.domain.usecase

/**
 * Complete pronunciation fallback map for all 28 Marungko phonemes.
 * Map contains realistic phonetic fallback variants (Filipino/English child speech patterns) for Vosk speech recognition.
 */
object PhonemePronunciationMap {
    val map: Map<String, List<String>> = mapOf(
        // Group 1
        "m" to listOf("m", "em", "um", "am", "ma"),
        "s" to listOf("s", "es", "suh", "is", "sa"),
        "a" to listOf("a", "ah", "uh", "apple", "ar"),
        "i" to listOf("i", "ee", "ih", "ey", "is"),

        // Group 2
        "o" to listOf("o", "oh", "aw", "or", "op"),
        "b" to listOf("b", "be", "bee", "buh", "ba"),
        "e" to listOf("e", "eh", "ed", "et", "el"),
        "u" to listOf("u", "uh", "oo", "up", "ur"),

        // Group 3
        "t" to listOf("t", "te", "tee", "tuh", "ta"),
        "k" to listOf("k", "ka", "kay", "kuh", "ke"),
        "l" to listOf("l", "el", "la", "luh", "lo"),
        "y" to listOf("y", "ye", "ya", "yuh", "why"),

        // Group 4
        "n" to listOf("n", "en", "na", "nuh", "no"),
        "g" to listOf("g", "ga", "ge", "guh", "go"),
        "ng" to listOf("ng", "nga", "ngauh", "eng", "ing"),
        "p" to listOf("p", "pe", "pa", "puh", "pi"),

        // Group 5
        "r" to listOf("r", "ar", "er", "ra", "ruh"),
        "d" to listOf("d", "de", "da", "duh", "di"),
        "h" to listOf("h", "ha", "eitch", "hah", "huh"),
        "w" to listOf("w", "wa", "wah", "wuh", "double u"),

        // Group 6
        "c" to listOf("c", "se", "ka", "see", "suh"),
        "f" to listOf("f", "ef", "fa", "fuh", "fe"),
        "j" to listOf("j", "jay", "je", "juh", "ja"),
        "ñ" to listOf("ñ", "enye", "nya", "nye", "nyo"),

        // Group 7
        "q" to listOf("q", "kyu", "kuh", "kw", "kwa"),
        "v" to listOf("v", "ve", "vee", "vuh", "va"),
        "x" to listOf("x", "eks", "ks", "ex", "eksuh"),
        "z" to listOf("z", "ze", "zee", "zed", "zuh")
    )
}
