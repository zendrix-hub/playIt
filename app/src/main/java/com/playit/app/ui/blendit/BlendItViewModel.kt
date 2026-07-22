package com.playit.app.ui.blendit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.audio.PhonemeAudioPlayer
import com.playit.app.domain.repository.PlayItRepository
import com.playit.app.domain.usecase.LetterCard
import com.playit.app.domain.usecase.SpellingEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BlendItUiState(
    val targetWords: List<String> = emptyList(),
    val currentWordIndex: Int = 0,
    val hearts: Int = 5,
    val isBlending: Boolean = false,
    val hasCompleted: Boolean = false,
    val scrambledLetters: List<LetterCard> = emptyList(),
    val spelledLetters: List<LetterCard> = emptyList(),
    val isError: Boolean = false
)

class BlendItViewModel(
    private val application: Application,
    private val repository: PlayItRepository,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlendItUiState())
    val uiState: StateFlow<BlendItUiState> = _uiState.asStateFlow()

    private val audioPlayer = PhonemeAudioPlayer(application)
    private val spellingEngine = SpellingEngine()

    init {
        loadWordsForPhoneme()
    }

    private fun loadWordsForPhoneme() {
        val dbGroupId = phonemeId.replace("BLEND_", "")
        viewModelScope.launch {
            // Load target words dynamically from database
            repository.getBlendItWords(dbGroupId).collect { wordsList ->
                val targetWords = wordsList.map { it.word }
                _uiState.update { state ->
                    val firstWord = targetWords.getOrNull(0) ?: "am"
                    state.copy(
                        targetWords = targetWords,
                        currentWordIndex = 0,
                        hearts = 5,
                        isBlending = false,
                        hasCompleted = false,
                        spelledLetters = emptyList(),
                        scrambledLetters = spellingEngine.scrambleWord(firstWord),
                        isError = false
                    )
                }
            }
        }
    }

    fun onLetterTapped(card: LetterCard) {
        val current = _uiState.value
        if (current.hasCompleted || current.isBlending || current.isError) return
        if (card.isUsed) return

        val newSpelled = current.spelledLetters + card.copy(isUsed = true)
        val newScrambled = current.scrambledLetters.map {
            if (it.id == card.id) it.copy(isUsed = true) else it
        }

        _uiState.update {
            it.copy(
                spelledLetters = newSpelled,
                scrambledLetters = newScrambled
            )
        }

        val currentWord = current.targetWords.getOrNull(current.currentWordIndex) ?: return
        if (newSpelled.size == currentWord.length) {
            val isCorrect = spellingEngine.validateSpelling(newSpelled, currentWord)
            if (isCorrect) {
                startBlending()
            } else {
                _uiState.update { it.copy(isError = true) }
            }
        }
    }

    fun onResetSpelling() {
        val current = _uiState.value
        if (current.isBlending || current.hasCompleted) return
        val currentWord = current.targetWords.getOrNull(current.currentWordIndex) ?: return
        _uiState.update {
            it.copy(
                spelledLetters = emptyList(),
                scrambledLetters = spellingEngine.scrambleWord(currentWord),
                isError = false
            )
        }
    }

    fun startBlending() {
        val currentState = _uiState.value
        if (currentState.targetWords.isEmpty() || currentState.currentWordIndex >= currentState.targetWords.size) return

        val currentWord = currentState.targetWords[currentState.currentWordIndex].lowercase()

        _uiState.update { it.copy(isBlending = true, hasCompleted = false, isError = false) }

        // Stage 1: Blend sound
        audioPlayer.playAssetAudio("audio/blend_$currentWord.mp3") {
            // Stage 2: Word sound
            audioPlayer.playAssetAudio("audio/word_$currentWord.mp3") {
                _uiState.update { it.copy(isBlending = false, hasCompleted = true) }
            }
        }
    }

    fun onSubmitClicked() {
        _uiState.update { currentState ->
            val nextIndex = currentState.currentWordIndex + 1
            val nextWord = currentState.targetWords.getOrNull(nextIndex) ?: ""
            currentState.copy(
                currentWordIndex = nextIndex,
                hasCompleted = false,
                isBlending = false,
                spelledLetters = emptyList(),
                scrambledLetters = spellingEngine.scrambleWord(nextWord),
                isError = false
            )
        }
    }

    fun completeSession() {
        val activeProfileId = com.playit.app.data.preferences.SessionManager.activeProfileId
        viewModelScope.launch {
            repository.updateBlendItProgress(
                com.playit.app.domain.model.BlendItProgress(
                    id = 0L,
                    profileId = activeProfileId,
                    groupId = phonemeId.replace("BLEND_", ""),
                    starsEarned = 3,
                    heartsLost = 0,
                    isCompleted = true,
                    completedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    class BlendItViewModelFactory(
        private val application: Application,
        private val repository: PlayItRepository,
        private val phonemeId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BlendItViewModel::class.java)) {
                return BlendItViewModel(application, repository, phonemeId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}