package com.playit.app.ui.map

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.repository.PlayItRepository
import com.playit.app.ui.map.MapNodeState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MapViewModel(
    private val repository: PlayItRepository
) : ViewModel() {

    // Automatically observe database changes and map them to UI node states
    val mapNodes: StateFlow<List<MapNodeState>> = repository.getUnlockedPhonemes()
        .map { progressList ->
            // 1. Define our structural sequence
            val sequence = listOf(
                "m", "s", "a", "i", "o", "BLEND_1",
                "b", "e", "u", "t", "k", "BLEND_2",
                "l", "y", "n", "g", "ng", "BLEND_3",
                "p", "r", "d", "h", "w", "BLEND_4",
                "c", "f", "j", "ñ", "q", "v", "x", "z", "BLEND_5"
            )

            // 2. Identify exactly which Group 1 pieces are fully done in the DB
            val group1Phonemes = listOf("m", "s", "a", "i", "o")
            val isGroup1Complete = group1Phonemes.all { corePhoneme ->
                progressList.any { it.phonemeId.lowercase() == corePhoneme && it.isCompleted }
            }

            // 3. Map out each node's availability state reactively
            sequence.mapIndexed { index, label ->
                val isBlend = label.startsWith("BLEND_")
                val dbRecord = progressList.find { it.phonemeId.lowercase() == label.lowercase() }

                val isFirst = index == 0

                // Standard linear unlock condition: previous item must be complete
                val previousCompleted = if (index > 0) {
                    val prevLabel = sequence[index - 1]
                    progressList.any { it.phonemeId.lowercase() == prevLabel.lowercase() && it.isCompleted }
                } else false

                val nodeUnlocked = when {
                    // If it's already marked complete in DB, it remains unlocked
                    dbRecord?.isCompleted == true -> true
                    // The gateway rule: BLEND_1 ONLY unlocks if all 5 letters are finished
                    label == "BLEND_1" -> isGroup1Complete
                    // Standard nodes unlock if they are first or the direct ancestor is cleared
                    isFirst || previousCompleted -> true
                    else -> false
                }

                MapNodeState(
                    id = index,
                    label = label,
                    isUnlocked = nodeUnlocked,
                    starsEarned = dbRecord?.starsEarned ?: 0,
                    isBlendIt = isBlend
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    class MapViewModelFactory(
        private val repository: com.playit.app.data.repository.PlayItRepository
    ) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(repository) as T
        }
    }
}