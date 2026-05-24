package com.playit.app.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.PlayItApplication
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// ─────────────────────────────────────────────
// Data model
// ─────────────────────────────────────────────

enum class NodeStatus { LOCKED, UNLOCKED, COMPLETED }

data class PhonemeNode(
    val phonemeId: String,
    val displayLetter: String,
    val status: NodeStatus,
    val stars: Int
)

data class MapUiState(
    val nodes: List<PhonemeNode> = emptyList(),
    val activeHearts: Int = 5,
    val totalStars: Int = 0,
    val currentStreak: Int = 0
)

// ─────────────────────────────────────────────
// ViewModel
// ─────────────────────────────────────────────

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PlayItApplication).repository

    // Combine AppConfig and Phoneme progression into one seamless UI State
    val uiState: StateFlow<MapUiState> = combine(
        repository.getUnlockedPhonemes(),
        repository.getAppConfig()
    ) { phonemes, config ->

        val totalStars = phonemes.sumOf { it.starsEarned }

        val mappedNodes = phonemes.map { dbNode ->
            val nodeStatus = when {
                dbNode.isCompleted -> NodeStatus.COMPLETED
                dbNode.isUnlocked -> NodeStatus.UNLOCKED
                else -> NodeStatus.LOCKED
            }

            PhonemeNode(
                phonemeId = dbNode.phonemeId,
                displayLetter = dbNode.letter,
                status = nodeStatus,
                stars = dbNode.starsEarned
            )
        }

        MapUiState(
            nodes = mappedNodes,
            totalStars = totalStars,
            currentStreak = config?.currentStreak ?: 0,
            activeHearts = 5
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MapUiState()
    )
}

class MapViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}