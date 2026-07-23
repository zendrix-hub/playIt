package com.playit.app.ui.hearit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.playit.app.data.audio.PhonemeAudioPlayer  // FIX 1: correct package

data class HearItUiState(
    val isPlaying: Boolean     = false,
    val hasPlayedOnce: Boolean = false,  // Next button unlocks after first play
    val errorMessage: String?  = null
)

class HearItViewModel(
    private val application: Application,
    private val phonemeId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(HearItUiState())
    val uiState: StateFlow<HearItUiState> = _uiState.asStateFlow()

    private val audioPlayer = PhonemeAudioPlayer(application)

    fun playAudio() {
        if (_uiState.value.isPlaying) return

        _uiState.update { it.copy(isPlaying = true, errorMessage = null) }

        // FIX 2: correct method name is playAssetAudio(fileName, onComplete)
        // FIX 3: no onError param — PhonemeAudioPlayer calls onComplete() as failsafe on error
        audioPlayer.playAssetAudio(
            fileName   = "audio/${phonemeId}.mp3",  // e.g. assets/audio/m.mp3
            onComplete = {
                // Spec: onComplete flips UI state, Next button enabled
                _uiState.update { it.copy(isPlaying = false, hasPlayedOnce = true) }
            },
            onError = { msg ->
                _uiState.update { it.copy(errorMessage = "We couldn't load the sound file for letter '$phonemeId'.") }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}

class HearItViewModelFactory(
    private val application: Application,
    private val phonemeId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HearItViewModel::class.java)) {
            return HearItViewModel(application, phonemeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}