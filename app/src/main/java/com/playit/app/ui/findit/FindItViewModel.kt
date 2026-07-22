package com.playit.app.ui.findit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.preferences.SessionManager
import com.playit.app.domain.model.LessonProgress
import com.playit.app.domain.repository.PlayItRepository
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
            val activeProfileId = SessionManager.activeProfileId
            // Priority 2 Fix: Only pick distractors the child has unlocked
            val unlockedPhonemes = repository.getUnlockedPhonemes(activeProfileId).first()
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

                val activeProfileId = SessionManager.activeProfileId
                viewModelScope.launch {
                    val existing = repository.getLessonProgress(activeProfileId, phonemeId)
                    repository.updateLessonProgress(
                        LessonProgress(
                            id = existing?.id ?: 0L,
                            profileId = activeProfileId,
                            phonemeId = phonemeId,
                            starsEarned = stars,
                            heartsLost = 5 - currentState.hearts,
                            isCompleted = true,
                            completedAt = System.currentTimeMillis()
                        )
                    )
                    repository.insertFindItAttempt(
                        com.playit.app.domain.model.FindItAttempt(
                            attemptId = 0L,
                            profileId = activeProfileId,
                            phonemeId = phonemeId,
                            selectedPhonemeId = item.id,
                            isCorrect = true,
                            attemptedAt = System.currentTimeMillis()
                        )
                    )
                }
                currentState.copy(
                    tapResults = newTapResults,
                    isComplete = true,
                    starsEarned = stars
                )
            } else {
                val newHearts = currentState.hearts - 1
                val activeProfileId = SessionManager.activeProfileId
                viewModelScope.launch {
                    repository.insertFindItAttempt(
                        com.playit.app.domain.model.FindItAttempt(
                            attemptId = 0L,
                            profileId = activeProfileId,
                            phonemeId = phonemeId,
                            selectedPhonemeId = item.id,
                            isCorrect = false,
                            attemptedAt = System.currentTimeMillis()
                        )
                    )
                }
                if (newHearts <= 0) {
                    currentState.copy(tapResults = newTapResults, hearts = 3)
                } else {
                    currentState.copy(tapResults = newTapResults, hearts = newHearts)
                }
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