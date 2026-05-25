package com.playit.app.ui.blendit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.playit.app.data.audio.PhonemeAudioPlayer

data class BlendItUiState(
    val targetWords: List<String> = emptyList(),
    val currentWordIndex: Int = 0,
    val hearts: Int = 5,
    val isBlending: Boolean = false, // Track active playback
    val hasCompleted: Boolean = false // Track if audio is finished
)

class BlendItViewModel(
    private val application: Application,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlendItUiState())
    val uiState: StateFlow<BlendItUiState> = _uiState.asStateFlow()

    // Instantiate the memory-safe player
    private val audioPlayer = PhonemeAudioPlayer(application)

    init {
        loadWordsForPhoneme()
    }

    private fun loadWordsForPhoneme() {
        // Populate list matching your exact audio assets from image_197b0a.png
        val targetWords = when (phonemeId.uppercase()) {
            "BLEND_1" -> listOf("am", "as", "is", "ma", "mac", "mass", "mom", "sam", "sis")
            else -> listOf("am", "sam") // Fallback
        }

        _uiState.update {
            it.copy(
                targetWords = targetWords,
                currentWordIndex = 0,
                hearts = 5,
                isBlending = false,
                hasCompleted = false
            )
        }
    }

    /**
     * Priority 1 Fix: Chained Audio Playback
     * Stage 1: Play blend (e.g., blend_sam.mp3)
     * Stage 2: Upon completion, play word (e.g., word_sam.mp3)
     * Stage 3: Upon completion, unlock the NEXT button
     */
    fun startBlending() {
        val currentState = _uiState.value
        if (currentState.targetWords.isEmpty() || currentState.currentWordIndex >= currentState.targetWords.size) return

        val currentWord = currentState.targetWords[currentState.currentWordIndex].lowercase()

        _uiState.update { it.copy(isBlending = true, hasCompleted = false) }

        // Start Stage 1: Blend Audio
        audioPlayer.playAssetAudio("audio/blend_$currentWord.mp3") {
            // Start Stage 2: Word Audio
            audioPlayer.playAssetAudio("audio/word_$currentWord.mp3") {
                // Final Stage: Unlock UI
                _uiState.update { it.copy(isBlending = false, hasCompleted = true) }
            }
        }
    }

    fun onSubmitClicked() {
        _uiState.update { currentState ->
            currentState.copy(
                currentWordIndex = currentState.currentWordIndex + 1,
                hasCompleted = false, // Reset for next word
                isBlending = false
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release() // Fix: Prevent memory leaks
    }

    class BlendItViewModelFactory(
        private val application: Application,
        private val phonemeId: String
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BlendItViewModel::class.java)) {
                return BlendItViewModel(application, phonemeId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}