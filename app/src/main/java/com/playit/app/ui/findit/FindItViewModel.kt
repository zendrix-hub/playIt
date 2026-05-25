package com.playit.app.ui.findit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.repository.PlayItRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─── Data Classes ─────────────────────────────────────────────────────────────
data class FindItGridItem(val id: String, val word: String)

data class FindItUiState(
    val gridItems: List<FindItGridItem> = emptyList(),
    val tapResults: Map<String, Boolean> = emptyMap(),
    val hearts: Int = 5,
    val isComplete: Boolean = false,
    val starsEarned: Int = 0
)

// ─── ViewModel ────────────────────────────────────────────────────────────────
class FindItViewModel(
    private val application: Application,
    private val repository: PlayItRepository,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(FindItUiState())
    val uiState: StateFlow<FindItUiState> = _uiState.asStateFlow()

    init {
        generateLineup()
    }

    private fun generateLineup() {
        viewModelScope.launch {
            // Priority 2 Fix: Only pick distractors the child has unlocked
            val unlockedPhonemes = repository.getUnlockedPhonemes().first()
            val unlockedLetters = unlockedPhonemes.map { it.letter.lowercase() }

            val target = phonemeId.lowercase()

            // Filter distractors from mastered letters
            val distractors = unlockedLetters
                .filter { it != target }
                .shuffled()
                .take(2)

            // Fallback: If this is the very first lesson, use safe defaults
            val options = (listOf(target) + if (distractors.size < 2) {
                listOf("a", "s").filter { it != target }.take(2)
            } else {
                distractors
            }).shuffled()

            val gridItems = options.map { letter ->
                FindItGridItem(id = letter, word = letter)
            }

            _uiState.update { it.copy(gridItems = gridItems) }
        }
    }

    fun onCardTapped(item: FindItGridItem) {
        if (_uiState.value.isComplete) return
        val isCorrect = item.id.lowercase() == phonemeId.lowercase()

        _uiState.update { currentState ->
            val newTapResults = currentState.tapResults.toMutableMap()
            newTapResults[item.id] = isCorrect

            if (isCorrect) {
                val stars = when (currentState.hearts) {
                    5 -> 3
                    4 -> 2
                    else -> 1
                }

                viewModelScope.launch {
                    repository.updateLessonProgress(
                        com.playit.app.data.local.entity.LessonProgress(
                            phonemeId = phonemeId, //
                            isCompleted = true,    //
                            starsEarned = stars    //
                        )
                    )
                }
                currentState.copy(
                    tapResults = newTapResults,
                    isComplete = true,
                    starsEarned = stars
                )
            } else {
                val newHearts = maxOf(0, currentState.hearts - 1)
                currentState.copy(tapResults = newTapResults, hearts = newHearts)
            }
        }
    }
}

// ─── Factory ──────────────────────────────────────────────────────────────────
class FindItViewModelFactory(
    private val application: Application,
    private val repository: PlayItRepository,
    private val phonemeId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindItViewModel::class.java)) {
            return FindItViewModel(application, repository, phonemeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}