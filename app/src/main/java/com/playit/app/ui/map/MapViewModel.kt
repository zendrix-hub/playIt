package com.playit.app.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.domain.model.Profile
import com.playit.app.domain.repository.PlayItRepository
import com.playit.app.domain.usecase.UnlockManager
import com.playit.app.domain.usecase.GroupUnlockManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    private val repository: PlayItRepository,
    private val profileId: Long
) : ViewModel() {

    private val unlockManager = UnlockManager()
    private val groupUnlockManager = GroupUnlockManager()

    // Dynamic reactive observation of active profile stats
    val activeProfile: StateFlow<Profile?> = repository.getAllProfiles()
        .map { list -> list.find { it.profileId == profileId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Automatically observe database changes and map them to UI node states
    val mapNodes: StateFlow<List<MapNodeState>> = repository.getUnlockedPhonemes(profileId)
        .map { progressList ->
            // Define our structural sequence (7 groups of 4 letters, with BLEND nodes in between)
            val sequence = listOf(
                "m", "s", "a", "i", "BLEND_1",
                "o", "b", "e", "u", "BLEND_2",
                "t", "k", "l", "y", "BLEND_3",
                "n", "g", "ng", "p", "BLEND_4",
                "r", "d", "h", "w", "BLEND_5",
                "c", "f", "j", "ñ", "BLEND_6",
                "q", "v", "x", "z", "BLEND_7"
            )

            // Dynamic completion mapping for groups to handle Blend It locks
            val groupLetters = mapOf(
                "BLEND_1" to listOf("m", "s", "a", "i"),
                "BLEND_2" to listOf("o", "b", "e", "u"),
                "BLEND_3" to listOf("t", "k", "l", "y"),
                "BLEND_4" to listOf("n", "g", "ng", "p"),
                "BLEND_5" to listOf("r", "d", "h", "w"),
                "BLEND_6" to listOf("c", "f", "j", "ñ"),
                "BLEND_7" to listOf("q", "v", "x", "z")
            )

            val completedPhonemes = progressList
                .filter { it.isCompleted }
                .map { it.phonemeId.lowercase() }
                .toSet()

            // 1. Map initial unlock status using domain UseCases
            val mappedNodes = sequence.mapIndexed { index, label ->
                val isBlend = label.startsWith("BLEND_")
                val dbRecord = progressList.find { it.phonemeId.lowercase() == label.lowercase() }
                val isCompleted = dbRecord?.isCompleted == true

                val nodeUnlocked = if (isBlend) {
                    val letters = groupLetters[label] ?: emptyList()
                    groupUnlockManager.isBlendItUnlocked(letters, completedPhonemes)
                } else {
                    unlockManager.isLetterUnlocked(label, sequence, completedPhonemes)
                }

                MapNodeState(
                    id = index,
                    label = label,
                    isUnlocked = nodeUnlocked,
                    starsEarned = dbRecord?.starsEarned ?: 0,
                    isBlendIt = isBlend,
                    isActiveNode = false // Default, will resolve next
                )
            }

            // 2. Identify the first incomplete, unlocked node as active (breathing animation state)
            val activeIdx = mappedNodes.indexOfFirst { it.isUnlocked && it.starsEarned == 0 }
            if (activeIdx != -1) {
                mappedNodes.mapIndexed { idx, node ->
                    if (idx == activeIdx) node.copy(isActiveNode = true) else node
                }
            } else {
                mappedNodes
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    class MapViewModelFactory(
        private val repository: PlayItRepository,
        private val profileId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(repository, profileId) as T
        }
    }
}