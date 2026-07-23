package com.playit.app.data.local

object BlendMasterDictionary {

    // 1. PURE DATA: Just a raw list of strings.
    // Because there is no filtering logic here, <clinit> will NEVER crash.
    val wordList = listOf(
        "am", "ma", "sam", "mom", "miss",
        "is", "as", "so", "mami", "misa"
    )

    // 2. RUNTIME LOGIC: Safely accepts the Set from the ViewModel.
    // This executes only when requested, completely bypassing the initialization trap.
    fun getAvailableWords(unlockedChars: Set<Char>): List<String> {
        // Failsafe: If the child has no letters unlocked, they get no words.
        if (unlockedChars.isEmpty()) return emptyList()

        return wordList.filter { word ->
            // Checks if EVERY single letter in the dictionary word
            // exists inside the child's unlocked Marungko letters.
            word.all { char -> unlockedChars.contains(char) }
        }
    }
}