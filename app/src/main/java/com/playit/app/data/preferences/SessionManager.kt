package com.playit.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

/**
 * Memory-safe singleton to keep track of the active profile session at runtime.
 * Because all sub-levels and progression map queries depend on the selected child,
 * this acts as the single source of truth for the active student context.
 *
 * Backed by DataStore<Preferences> so activeProfileId survives process death.
 */
object SessionManager {
    private val KEY_ACTIVE_PROFILE_ID = longPreferencesKey("active_profile_id")
    private var dataStore: DataStore<Preferences>? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    @Volatile
    private var _activeProfileId: Long = -1L

    var activeProfileId: Long
        get() = _activeProfileId
        set(value) {
            _activeProfileId = value
            dataStore?.let { store ->
                scope.launch {
                    store.edit { preferences ->
                        preferences[KEY_ACTIVE_PROFILE_ID] = value
                    }
                }
            }
        }

    val isProfileSelected: Boolean
        get() = activeProfileId != -1L

    fun init(context: Context) {
        if (dataStore != null) return
        val store = context.applicationContext.dataStore
        dataStore = store
        _activeProfileId = runBlocking(Dispatchers.IO) {
            store.data.map { preferences ->
                preferences[KEY_ACTIVE_PROFILE_ID] ?: -1L
            }.first()
        }
    }

    fun clearSession() {
        activeProfileId = -1L
    }
}
