package com.playit.app.ui.blendit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BlendItUiState(
    val targetWords: List<String> = emptyList(),
    val currentWordIndex: Int = 0,
    val hearts: Int = 5
)

class BlendItViewModel(
    private val application: Application,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(BlendItUiState())
    val uiState: StateFlow<BlendItUiState> = _uiState.asStateFlow()

    // TODO: If you are injecting PhonemeAudioPlayer via DI, you can do it here.
    // Otherwise, instantiate it like this based on your project state:
    // private val audioPlayer = PhonemeAudioPlayer(application)

    init {
        loadWordsForPhoneme()
    }

    private fun loadWordsForPhoneme() {
        // MVP Integration: Grab 3 words for the current checkpoint.
        // Replace this block with your actual BlendMasterDictionary lookup.
        val targetWords = when (phonemeId.lowercase()) {
            "m" -> listOf("am", "ma", "mac")
            "s" -> listOf("sam", "mass", "sas")
            "a" -> listOf("sam", "am", "as")
            else -> listOf("cat", "bat", "rat") // Fallback
        }

        _uiState.update {
            it.copy(
                targetWords = targetWords,
                currentWordIndex = 0,
                hearts = 5 // Blend It provides 5 fresh hearts per checkpoint
            )
        }
    }

    fun replayAudio() {
        val currentState = _uiState.value
        if (currentState.targetWords.isEmpty() || currentState.currentWordIndex >= currentState.targetWords.size) return

        val currentWord = currentState.targetWords[currentState.currentWordIndex]

        // Trigger your audio player to play the ElevenLabs dashed audio file.
        // e.g., audioPlayer.play("audio/blend_${currentWord.lowercase()}.mp3")
        println("▶️ PLAYING BLEND AUDIO: blend_$currentWord.mp3")
    }

    fun onSubmitClicked() {
        // Advances to the next word. The UI will catch when index >= size and call onSessionComplete()
        _uiState.update { currentState ->
            currentState.copy(currentWordIndex = currentState.currentWordIndex + 1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // audioPlayer.release() // Don't forget to clean up the MediaPlayer!
    }
}

class BlendItViewModelFactory(
    private val application: Application,
    private val phonemeId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BlendItViewModel(application, phonemeId) as T
    }
}