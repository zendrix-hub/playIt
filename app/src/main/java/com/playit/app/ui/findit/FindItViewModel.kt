package com.playit.app.ui.findit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Simple data class representing the grid items
data class FindItGridItem(val id: String, val word: String)

data class FindItUiState(
    val gridItems: List<FindItGridItem> = emptyList(),
    val tapResults: Map<String, Boolean> = emptyMap(), // Maps the tapped letter ID to true (correct) or false (wrong)
    val hearts: Int = 5,
    val isComplete: Boolean = false,
    val starsEarned: Int = 0
)

class FindItViewModel(
    private val application: Application,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindItUiState())
    val uiState: StateFlow<FindItUiState> = _uiState.asStateFlow()

    // The complete Marungko Sequence for generating random distractors
    private val marungkoSequence = listOf(
        "m", "s", "a", "i", "o", "b", "e", "u", "t", "k", "l", "y", "n",
        "g", "ng", "p", "r", "d", "h", "w", "c", "f", "j", "ñ", "q", "v", "x", "z"
    )

    init {
        generateLineup()
    }

    private fun generateLineup() {
        val target = phonemeId.lowercase()

        // 1. Pick 2 random distractors that are NOT the target letter
        val distractors = marungkoSequence
            .filter { it != target }
            .shuffled()
            .take(2)

        // 2. Combine the target and distractors, then shuffle their positions!
        val options = (listOf(target) + distractors).shuffled()

        // 3. Map to our GridItem format so the UI can read it
        val gridItems = options.map { letter ->
            FindItGridItem(
                id = letter,
                word = letter
            )
        }

        _uiState.update { it.copy(gridItems = gridItems) }
    }

    fun onCardTapped(item: FindItGridItem) {
        // Prevent interactions if they've already won
        if (_uiState.value.isComplete) return

        val isCorrect = item.id.lowercase() == phonemeId.lowercase()

        _uiState.update { currentState ->
            val newTapResults = currentState.tapResults.toMutableMap()
            newTapResults[item.id] = isCorrect

            if (isCorrect) {
                // Determine stars based on how many hearts they have left
                val stars = when (currentState.hearts) {
                    5 -> 3
                    4 -> 2
                    else -> 1
                }
                currentState.copy(
                    tapResults = newTapResults,
                    isComplete = true,
                    starsEarned = stars
                )
            } else {
                // Deduct a heart for a wrong answer
                val newHearts = maxOf(0, currentState.hearts - 1)
                currentState.copy(
                    tapResults = newTapResults,
                    hearts = newHearts
                )
            }
        }
    }
}

class FindItViewModelFactory(
    private val application: Application,
    private val phonemeId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FindItViewModel(application, phonemeId) as T
    }
}