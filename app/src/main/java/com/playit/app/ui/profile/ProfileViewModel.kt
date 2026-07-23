package com.playit.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.data.preferences.SessionManager
import com.playit.app.domain.model.Profile
import com.playit.app.domain.repository.PlayItRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val repository: PlayItRepository
) : ViewModel() {

    // Automatically observe database profiles
    val profiles: StateFlow<List<Profile>> = repository.getAllProfiles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun selectProfile(profileId: Long) {
        SessionManager.activeProfileId = profileId
    }

    fun createProfile(name: String, avatarResId: Int, onComplete: (Long) -> Unit) {
        android.util.Log.d("PlayItDebug", "ProfileViewModel.createProfile called for name='$name'")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentProfiles = repository.getAllProfiles().first()
                android.util.Log.d("PlayItDebug", "Current profiles count: ${currentProfiles.size}")
                if (currentProfiles.size >= 6) {
                    android.util.Log.w("PlayItDebug", "Max profiles (6) reached, aborting creation")
                    return@launch
                }

                val newProfile = Profile(
                    profileId = 0L,
                    name = name.trim(),
                    avatarResId = avatarResId,
                    totalStars = 0,
                    currentStreak = 0,
                    lastPlayedAt = null,
                    createdAt = System.currentTimeMillis()
                )
                val generatedId = repository.insertProfile(newProfile)
                android.util.Log.d("PlayItDebug", "Profile inserted with ID=$generatedId")
                SessionManager.activeProfileId = generatedId
                android.util.Log.d("PlayItDebug", "SessionManager activeProfileId set to $generatedId")
                withContext(Dispatchers.Main) {
                    onComplete(generatedId)
                }
            } catch (e: Exception) {
                android.util.Log.e("PlayItDebug", "Error during profile creation", e)
            }
        }
    }

    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            repository.deleteProfile(profileId)
            if (SessionManager.activeProfileId == profileId) {
                SessionManager.clearSession()
            }
        }
    }

    class ProfileViewModelFactory(
        private val repository: PlayItRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(repository) as T
        }
    }
}

