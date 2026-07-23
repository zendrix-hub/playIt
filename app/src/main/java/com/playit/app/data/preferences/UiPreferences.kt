package com.playit.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.uiDataStore: DataStore<Preferences> by preferencesDataStore(name = "ui_preferences")

/**
 * DataStore-backed preference manager for UI and accessibility settings (such as reduced motion).
 * Survives process death and app restarts.
 */
class UiPreferences(context: Context) {
    private val dataStore = context.applicationContext.uiDataStore

    /**
     * Emits true when the learner or parent has enabled Reduced Motion mode.
     */
    val reducedMotionEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_REDUCED_MOTION] ?: false
    }

    /**
     * Persists the user's reduced motion preference.
     */
    suspend fun setReducedMotionEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_REDUCED_MOTION] = enabled
        }
    }

    companion object {
        private val KEY_REDUCED_MOTION = booleanPreferencesKey("reduced_motion_enabled")

        @Volatile
        private var INSTANCE: UiPreferences? = null

        fun getInstance(context: Context): UiPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UiPreferences(context).also { INSTANCE = it }
            }
        }
    }
}
