package com.playit.app.data.preferences

/**
 * Memory-safe singleton to keep track of the active profile session at runtime.
 * Because all sub-levels and progression map queries depend on the selected child,
 * this acts as the single source of truth for the active student context.
 */
object SessionManager {
    var activeProfileId: Long = -1L

    val isProfileSelected: Boolean
        get() = activeProfileId != -1L

    fun clearSession() {
        activeProfileId = -1L
    }
}
