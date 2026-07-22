package com.playit.app.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.playit.app.PlayItApplication
import com.playit.app.data.preferences.SessionManager
import com.playit.app.domain.model.Profile
import com.playit.app.domain.repository.PlayItRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application,
    private val repository: PlayItRepository
) : AndroidViewModel(application) {

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
        val currentProfiles = profiles.value
        if (currentProfiles.size >= 6) return // Enforce maximum of 6 profiles

        viewModelScope.launch {
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
            SessionManager.activeProfileId = generatedId
            onComplete(generatedId)
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
        private val application: Application,
        private val repository: PlayItRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(application, repository) as T
        }
    }
}
