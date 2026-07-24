package com.playit.app.ui.a11y

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.playit.app.ui.theme.LocalReducedMotion

/**
 * Helper accessor for querying the global Reduced Motion accessibility state.
 * When enabled, decorative animations, particle explosions, and parallax loops
 * are suppressed or replaced with static layouts to avoid sensory overload.
 */
object ReducedMotionState {
    val current: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalReducedMotion.current
}
